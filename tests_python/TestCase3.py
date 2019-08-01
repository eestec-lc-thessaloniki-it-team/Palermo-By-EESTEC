import sys

import requests

from printing import printin


def testCase3(serverIP, username, password, username2, password2):
    '''
    This test case will demonstrate the begining of a game. 2 users will connect. Both will try to start the game,
    but only the leader should succeed. N end point should exist that will return the info for the game. When the game
    starts appropriate the roles should be given to players and update their user_to_game record
    :param serverIP: servers ip that app is uploaded
    :param username: username of user
    :param password: password of user
    :param username2:  username of user2
    :param password2: password of user2
    :return:
    '''

    print("\nTestCase#3 The game starts...\n")

    r = requests.post("http://" + serverIP + ":8080/palermo/api/v1/user/newUser",
                      json={"username": username, "password": password})
    if r.status_code != 200: sys.exit("Error with initialization user1")
    userID1 = r.json()

    r = requests.post("http://" + serverIP + ":8080/palermo/api/v1/user/newUser",
                      json={"username": username2, "password": password2})
    if r.status_code != 200: sys.exit("Error with initialization user2")
    userID2 = r.json()

    print("Initialization ...OK")

    r = requests.post("http://" + serverIP + ":8080/palermo/api/v1/game/newGame", json=userID1)
    if r.status_code != 200: sys.exit("There was an error with creating a new game")
    gameID = r.json()['random_id']

    r = requests.post("http://" + serverIP + ":8080/palermo/api/v1/game/joinGame/" + gameID + "D", json=userID2)
    printin(r.status_code == 404, "Joining an no-existing game")

    r = requests.post("http://" + serverIP + ":8080/palermo/api/v1/ingame/info", json=userID2)
    printin(r.status_code == 400, "Getting info of a non existed game")

    r = requests.post("http://" + serverIP + ":8080/palermo/api/v1/game/gameInfo", json=userID2)
    printin(r.status_code == 400, "Ask for gameInfo and you are not connected to a game")

    r = requests.post("http://" + serverIP + ":8080/palermo/api/v1/game/joinGame/" + gameID, json=userID2)
    if r.status_code != 200: sys.exit("There was an error with joining the correct game")

    r = requests.post("http://" + serverIP + ":8080/palermo/api/v1/game/gameInfo", json=userID1)
    printin(r.status_code == 200 and not r.json()['started'], "Game Info request")

    r = requests.post("http://" + serverIP + ":8080/palermo/api/v1/game/startGame", json=userID2)
    printin(r.status_code == 401, "A no leader try to start the game")

    r = requests.post("http://" + serverIP + ":8080/palermo/api/v1/game/startGame", json=userID1)
    if r.status_code == 505:
        print("There was an error while parsing data from roles.txt")
        return
    printin(r.status_code == 200 and r.json()['started'], "Leader starts the game")

    r = requests.post("http://" + serverIP + ":8080/palermo/api/v1/game/gameInfo", json=userID1)
    printin(r.status_code == 200 and r.json()['started'] , "Game Info request")
    printin(r.json()['state'] == "Night","Initial state is Night")

    r = requests.post("http://" + serverIP + ":8080/palermo/api/v1/ingame/nextState", json=userID1)
    printin(r.status_code == 200 and r.json()['state'] == "Morning", "Second is Morning")

    r = requests.post("http://" + serverIP + ":8080/palermo/api/v1/ingame/nextState", json=userID1)
    printin(r.status_code == 200 and r.json()['state'] == "Voting", "Third is Voting")

    r = requests.post("http://" + serverIP + ":8080/palermo/api/v1/ingame/nextState", json=userID1)
    printin(r.status_code == 200 and r.json()['state'] == "Night", "Back to Night after 3 changes")

    r = requests.post("http://" + serverIP + ":8080/palermo/api/v1/ingame/info/", json=userID1)
    printin(r.status_code == 200 and (r.json()['role_type'] == "Murderer" or r.json()['role_type'] == "Policeman"),
            "User1 got role")

    r = requests.post("http://" + serverIP + ":8080/palermo/api/v1/ingame/info", json=userID2)
    printin(r.status_code == 200 and (r.json()['role_type'] == "Murderer" or r.json()['role_type'] == "Policeman"),
            "User2 got role")

    r = requests.post("http://" + serverIP + ":8080/palermo/api/v1/user/logOut", json=userID1)
    if r.status_code != 200: sys.exit("Error with clearing data")

    r = requests.post("http://" + serverIP + ":8080/palermo/api/v1/user/logOut", json=userID2)
    if r.status_code != 200: sys.exit("Error with clearing data")

    print("Clearing data ... OK")
