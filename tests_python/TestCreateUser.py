import requests


# serverIP = "127.0.0.1"


def printin(isOK, nameTest):
    if isOK:
        print(nameTest + "... OK")
    else:
        print(nameTest + "... NOT OK")


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
    printin(r.status_code == 200, "Logging out a already connected User")


def deletingUser(serverIP, userToken):
    r = requests.post("http://" + serverIP + ":8080/palermo/api/v1/user/deleteUser", json=userToken)
    printin(r.status_code == 200, "Deleting user")
