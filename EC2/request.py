import requests

url = "http://18.191.252.222:8000/token"
response = requests.get(url)

if response.status_code == 200:
    data = response.json()
    print(data)
else:
    print("Falha na solicitação.")
