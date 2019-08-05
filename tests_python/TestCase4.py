import sys

import requests

from printing import printin


def testCase4(serverIP, username, password, username2, password2, username3, password3):
    '''
    This test case will connect 3 users to a game, give them roles and then test what will happen at night for every role

    :param serverIP:
    :param username:
    :param password:
    :param username2:
    :param password2:
    :param password3:
    :param username3:
    :return:
    '''

    victim = None
    murderer = None
    policeman = None

    print("\nTestCase#4 The night will take place....\n")

    r = requests.post("http://" + serverIP + ":8080/palermo/api/v1/user/newUser",
                      json={"username": username, "password": password})
    if r.status_code != 200: sys.exit("Error with initialization user1")
    userID1 = r.json()

    r = requests.post("http://" + serverIP + ":8080/palermo/api/v1/user/newUser",
                      json={"username": username2, "password": password2})
    if r.status_code != 200: sys.exit("Error with initialization user2")
    userID2 = r.json()

    r = requests.post("http://" + serverIP + ":8080/palermo/api/v1/user/newUser",
                      json={"username": username3, "password": password3})
    if r.status_code != 200: sys.exit("Error with initialization user2")
    userID3 = r.json()

    r = requests.post("http://" + serverIP + ":8080/palermo/api/v1/game/newGame", json=userID1)
    if r.status_code != 200: sys.exit("There was an error with creating a new game")
    gameID = r.json()['random_id']

    r = requests.post("http://" + serverIP + ":8080/palermo/api/v1/game/joinGame/" + gameID, json=userID2)
    if r.status_code != 200: sys.exit("There was an error with joining the correct game")

    r = requests.post("http://" + serverIP + ":8080/palermo/api/v1/game/joinGame/" + gameID, json=userID3)
    if r.status_code != 200: sys.exit("There was an error with joining the correct game")

    r = requests.post("http://" + serverIP + ":8080/palermo/api/v1/game/startGame", json=userID1)
    if r.status_code == 505:
        print("There was an error while parsing data from roles.txt")
        return
    printin(r.status_code == 200 and r.json()['started'], "Leader starts the game")

    print("Initialization ...OK")

    r = requests.post("http://" + serverIP + ":8080/palermo/api/v1/ingame/state", json=userID1)
    printin(r.status_code == 200 and r.json()['state'] == "Night", "Ask for game state")

    r = requests.post("http://" + serverIP + ":8080/palermo/api/v1/ingame/roleInfo", json=userID1)
    printin(r.status_code == 200 and "roleName" in r.json() and "roleTeam" in r.json() and "description" in r.json(),
            "Get role info for user 1")
    if r.json()["roleName"] == "Victim":
        victim = userID1
    elif r.json()["roleName"] == "Murderer":
        murderer = userID1
    else:
        policeman = userID1

    r = requests.post("http://" + serverIP + ":8080/palermo/api/v1/ingame/roleInfo", json=userID2)
    printin(r.status_code == 200 and "roleName" in r.json() and "roleTeam" in r.json() and "description" in r.json(),
            "Get role info for user 2")
    if r.json()["roleName"] == "Victim":
        victim = userID2
    elif r.json()["roleName"] == "Murderer":
        murderer = userID2
    else:
        policeman = userID2

    r = requests.post("http://" + serverIP + ":8080/palermo/api/v1/ingame/roleInfo", json=userID3)
    printin(r.status_code == 200 and "roleName" in r.json() and "roleTeam" in r.json() and "description" in r.json(),
            "Get role info for user 3")
    if r.json()["roleName"] == "Victim":
        victim = userID3
    elif r.json()["roleName"] == "Murderer":
        murderer = userID3
    else:
        policeman = userID3

    # up untill here we should have all the available roles
    # Test actions
    usernames = {"usernames": [username2]}
    r = requests.post("http://" + serverIP + ":8080/palermo/api/v1/ingame/act/test2,test3", json=victim)
    if r.status_code == 200:
        print(r.status_code)

    r = requests.post("http://" + serverIP + ":8080/palermo/api/v1/user/logOut", json=userID1)
    if r.status_code != 200: sys.exit("Error with clearing data")

    r = requests.post("http://" + serverIP + ":8080/palermo/api/v1/user/logOut", json=userID2)
    if r.status_code != 200: sys.exit("Error with clearing data")

    r = requests.post("http://" + serverIP + ":8080/palermo/api/v1/user/logOut", json=userID3)
    if r.status_code != 200: sys.exit("Error with clearing data")

    print("Clearing data ... OK")
