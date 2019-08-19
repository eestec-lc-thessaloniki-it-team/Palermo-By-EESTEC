import sys

import requests
from TestingChangeStates import testChangeState
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
    print(r.json())

    print("Info about the role: ")
    r = requests.post("http://" + serverIP + ":8080/palermo/api/v1/ingame/roleInfo", json=userID1)
    printin(r.status_code == 200 and "roleName" in r.json() and "roleTeam" in r.json() and "description" in r.json(),
            "Get role info for user 1")
    if r.json()["roleName"] == "Victim":
        victim = userID1
    elif r.json()["roleName"] == "Murderer":
        murderer = userID1
    else:
        policeman = userID1
    print(r.json())
    r = requests.post("http://" + serverIP + ":8080/palermo/api/v1/ingame/roleInfo", json=userID2)
    printin(r.status_code == 200 and "roleName" in r.json() and "roleTeam" in r.json() and "description" in r.json(),
            "Get role info for user 2")
    if r.json()["roleName"] == "Victim":
        victim = userID2
    elif r.json()["roleName"] == "Murderer":
        murderer = userID2
    else:
        policeman = userID2
    print(r.json())

    r = requests.post("http://" + serverIP + ":8080/palermo/api/v1/ingame/roleInfo", json=userID3)
    printin(r.status_code == 200 and "roleName" in r.json() and "roleTeam" in r.json() and "description" in r.json(),
            "Get role info for user 3")
    if r.json()["roleName"] == "Victim":
        victim = userID3
    elif r.json()["roleName"] == "Murderer":
        murderer = userID3
    else:
        policeman = userID3
    print(r.json())

    # up until here we should have all the available roles
    # Test actions
    r = requests.post("http://" + serverIP + ":8080/palermo/api/v1/ingame/act",
                      json={"userToken": victim, "ids": [policeman["user_id"]]})
    printin(r.status_code == 200, "Victim is taking an action")

    r = requests.post("http://" + serverIP + ":8080/palermo/api/v1/ingame/info", json=victim)
    printin(r.status_code == 200, "Victim has acted at night")
    print(r.json())

    r = requests.post("http://" + serverIP + ":8080/palermo/api/v1/ingame/act",
                      json={"userToken": policeman, "ids": [victim["user_id"]]})
    printin(r.status_code == 200 and not r.json()["isMurderer"], "Policeman asked for a Victim")

    r = requests.post("http://" + serverIP + ":8080/palermo/api/v1/ingame/act",
                      json={"userToken": policeman, "ids": [murderer["user_id"]]})
    printin(r.status_code == 200 and r.json()["isMurderer"], "Policeman asked for a Murderer")

    r = requests.post("http://" + serverIP + ":8080/palermo/api/v1/ingame/act",
                      json={"userToken": murderer, "ids": [policeman["user_id"]]})
    printin(r.status_code == 200 and len(r.json()) == 3, "Murderer vote a player to die")

    r = requests.post("http://" + serverIP + ":8080/palermo/api/v1/ingame/nightInfo",
                      json=murderer)
    printin(r.status_code == 200 and "murdererVotes" in r.json(), "Murderer vote a player to die")
    print("Votes : ")
    for element in r.json()["murdererVotes"]:
        print(element)

    r = requests.post("http://" + serverIP + ":8080/palermo/api/v1/ingame/nextState",
                      json=userID1)
    printin(r.status_code == 200 and r.json()["is_game_over"], "A new day in palermo")

    testChangeState(serverIP,userID1)

    r = requests.post("http://" + serverIP + ":8080/palermo/api/v1/ingame/state",
                      json=userID2)
    printin(r.status_code == 200 and r.json()["is_game_over"], "Now everyone knows that the game is over")
    print(r.json()["won"])

    r = requests.post("http://" + serverIP + ":8080/palermo/api/v1/user/logOut", json=userID1)
    if r.status_code != 200: sys.exit("Error with clearing data")

    r = requests.post("http://" + serverIP + ":8080/palermo/api/v1/user/logOut", json=userID2)
    if r.status_code != 200: sys.exit("Error with clearing data")

    r = requests.post("http://" + serverIP + ":8080/palermo/api/v1/user/logOut", json=userID3)
    if r.status_code != 200: sys.exit("Error with clearing data")

    print("Clearing data ... OK")
