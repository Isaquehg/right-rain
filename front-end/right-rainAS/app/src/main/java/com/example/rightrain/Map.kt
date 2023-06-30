package com.example.rightrain

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.os.Bundle
import androidx.annotation.DrawableRes
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.content.res.AppCompatResources
import com.mapbox.geojson.Point
import com.mapbox.maps.CameraOptions
import com.mapbox.maps.MapView
import com.mapbox.maps.Style
import com.mapbox.maps.plugin.annotation.annotations
import com.mapbox.maps.plugin.annotation.generated.PointAnnotationOptions
import com.mapbox.maps.plugin.annotation.generated.createPointAnnotationManager

 class Map : AppCompatActivity() {

        fun addAnnotationToMap(context: Context, mapView: MapView, coordinates: List<Pair<Double, Double>>) {
            mapView!!.getMapboxMap().setCamera(
                CameraOptions.Builder()
                    .center(Point.fromLngLat(-45.7037, -22.2522))
                    .zoom(11.0)
                    .build()
            )
// Create an instance of the Annotation API and get the PointAnnotationManager.
            bitmapFromDrawableRes(
                context,
                R.drawable.red_marker
            )?.let {
                val annotationApi = mapView?.annotations
                val pointAnnotationManager = annotationApi?.createPointAnnotationManager(mapView!!)
                coordinates.forEach { coordinate ->
// Set options for the resulting symbol layer.
                    val pointAnnotationOptions: PointAnnotationOptions = PointAnnotationOptions()
// Define a geographic coordinate.
                        .withPoint(Point.fromLngLat(coordinate.second, coordinate.first))
// Specify the bitmap you assigned to the point annotation
// The bitmap will be added to map style automatically.
                        .withIconImage(it)
// Add the resulting pointAnnotation to the map.
                    pointAnnotationManager?.create(pointAnnotationOptions)
                }
            }
        }
        fun bitmapFromDrawableRes(context: Context, @DrawableRes resourceId: Int) =
            convertDrawableToBitmap(AppCompatResources.getDrawable(context, resourceId))

        fun convertDrawableToBitmap(sourceDrawable: Drawable?): Bitmap? {
            if (sourceDrawable == null) {
                return null
            }
            return if (sourceDrawable is BitmapDrawable) {
                sourceDrawable.bitmap
            } else {
// copying drawable object to not manipulate on the same reference
                val constantState = sourceDrawable.constantState ?: return null
                val drawable = constantState.newDrawable().mutate()
                val bitmap: Bitmap = Bitmap.createBitmap(
                    drawable.intrinsicWidth, drawable.intrinsicHeight,
                    Bitmap.Config.ARGB_8888
                )
                val canvas = Canvas(bitmap)
                drawable.setBounds(0, 0, canvas.width, canvas.height)
                drawable.draw(canvas)
                bitmap
            }
        }
 }