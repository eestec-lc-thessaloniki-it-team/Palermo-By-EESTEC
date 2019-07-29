import requests
from printing import printin


def testLogIn(serverIP, username, password):
    print("\nTest cases for LogIn    \n ")

    r = requests.post("http://" + serverIP + ":8080/palermo/api/v1/user/logIn",
                      json={"username": "", "password": password})
    printin(r.status_code == 400, "Logging in user without username")

    r = requests.post("http://" + serverIP + ":8080/palermo/api/v1/user/logIn",
                      json={"username": username, "password": ""})
    printin(r.status_code == 400, "Logging in user without password")

    r = requests.post("http://" + serverIP + ":8080/palermo/api/v1/user/logIn",
                      json={"username": username, "password": password + "1"})
    printin(r.status_code == 410, "logging in user with false password")

    r = requests.post("http://" + serverIP + ":8080/palermo/api/v1/user/logIn",
                      json={"username": username, "password": password})
    printin(r.status_code == 200, "logging in user with correct password")
    userID = r.json()

    r = requests.post("http://" + serverIP + ":8080/palermo/api/v1/user/logIn",
                      json={"username": username, "password": password})
    # Checking also if the user id remains the same but the token changes so now database has the new one
    printin(r.status_code == 200 and userID['user_id'] == r.json()['user_id'] and userID['token'] != r.json()['token'],
            "logging in user that is already connected")
    userID = r.json()

    r = requests.post("http://" + serverIP + ":8080/palermo/api/v1/user/logOut", json=userID)
    printin(r.status_code == 200, "Log out")
