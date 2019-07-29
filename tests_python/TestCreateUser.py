import sys

import requests

from printing import printin


def testCreateUser(serverIP, username, password):
    print("Test cases for New User    \n ")

    r = requests.post("http://" + serverIP + ":8080/palermo/api/v1/user/newUser",
                      json={"username": "", "password": password})
    printin(r.status_code == 400, "Adding user without username")

    r = requests.post("http://" + serverIP + ":8080/palermo/api/v1/user/newUser",
                      json={"username": username, "password": ""})
    printin(r.status_code == 400, "Adding user without password")

    r = requests.post("http://" + serverIP + ":8080/palermo/api/v1/user/newUser",
                      json={"username": "tester", "password": password})
    printin(r.status_code == 406, "Adding user with already existed username")

    r = requests.post("http://" + serverIP + ":8080/palermo/api/v1/user/newUser",
                      json={"username": username, "password": password})
    printin(r.status_code == 200, "Adding correct user")
    userID = r.json()

    r = requests.post("http://" + serverIP + ":8080/palermo/api/v1/user/logOut", json=userID)
    printin(r.status_code == 200, "Log out")


def deletingUser(serverIP, username, password):
    r = requests.post("http://" + serverIP + ":8080/palermo/api/v1/user/logIn",
                      json={"username": username, "password": password})
    if r.status_code != 200: sys.exit("Error with logging to user "+username)
    userToken = r.json()

    r = requests.post("http://" + serverIP + ":8080/palermo/api/v1/user/deleteUser", json=userToken)
    if r.status_code != 200: sys.exit("Error with deleting to user "+username)
