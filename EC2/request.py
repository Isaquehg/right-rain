import requests
'''
url = "http://18.191.252.222:80/token"
response = requests.get(url)

if response.status_code == 200:
    data = response.json()
    print(data)
else:
    print("Falha na solicitação.")'''

# Na Activity de login
payload = alguma_funcao_que_converte_p_json(username, senha)
url = "http://18.191.252.222:80/token"
response = requests.post(url, json = payload)
print(response)
'''
{
    "access_token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiJnYXJyeUBnbWFpbC5jb20iLCJleHAiOjE2ODg1MDMzMTB9.EBMBHZe7_Abxl0XtTZ-tn42EI3o-KpY2q0yejxHVpvs",
    "token_type": "bearer"
}
'''

if response.status_code == 200:
    data = response.json()
    user_key = data.bearear
    user_id = data.u_id
else:
    print("Falha na solicitação.")

# Home page
if(user_key != None):
    url = "http://18.191.252.222:80/{u_id}"
    response = requests.get(url, header = user_key)
    devices = response.json()
    
