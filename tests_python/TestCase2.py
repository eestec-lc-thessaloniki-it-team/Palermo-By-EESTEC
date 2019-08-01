import sys

import requests

from printing import printin


def testcase2(serverIP, username, password, username2, password2):
    '''
    This Case will demonstrate a user that will log in to the app, create a game and then leave
    :param serverIP: servers ip
    :param username: username of leader that will create game
    :param password: password of leader
    :param username2: username of a second player
    :param password2: password of second player
    :return:
    '''

    print("\nTest case #2 A user that will log in to the app, create a game and then leave \n")

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
    printin(r.status_code == 200 and r.json()['leader_id'] == userID1['user_id'], "Creating a new game")
    gameID = r.json()['random_id']
    leaderID = r.json()['leader_id']

    r = requests.post("http://" + serverIP + ":8080/palermo/api/v1/game/getUsers", json=userID1)
    printin(r.status_code == 200 and len(r.json()) == 1 and r.json()[0] == username,
            "Getting users in game when I am solo")

    r = requests.post("http://" + serverIP + ":8080/palermo/api/v1/game/joinGame/" + gameID, json=userID2)
    printin(r.status_code == 200, "Joining a game")

    r = requests.post("http://" + serverIP + ":8080/palermo/api/v1/game/getUsers", json=userID1)
    printin(r.status_code == 200 and len(r.json()) == 2, "Getting users after a joining")

    r = requests.post("http://" + serverIP + ":8080/palermo/api/v1/user/logOut", json=userID2)
    if r.status_code != 200: sys.exit("Error with logging out")


    r = requests.post("http://" + serverIP + ":8080/palermo/api/v1/game/getUsers", json=userID1)
    printin(r.status_code == 200 and len(r.json()) == 1 and r.json()[0] == username,
            "Logging out removing a player from a game")

    r = requests.post("http://" + serverIP + ":8080/palermo/api/v1/user/logIn",
                      json={"username": username2, "password": password2})
    userID2=r.json()
    r = requests.post("http://" + serverIP + ":8080/palermo/api/v1/game/joinGame/" + gameID, json=userID2)
    r = requests.post("http://" + serverIP + ":8080/palermo/api/v1/game/getUsers", json=userID1)
    printin(r.status_code == 200 and len(r.json()) == 2, "After rejoing should be 2 again")

    r = requests.post("http://" + serverIP + ":8080/palermo/api/v1/user/logOut", json=userID1)
    if r.status_code != 200: sys.exit("Error with logging out")
    r = requests.post("http://" + serverIP + ":8080/palermo/api/v1/game/getUsers", json=userID2)
    printin(r.status_code == 200 and len(r.json()) == 1 and r.json()[0] == username2,
            "After leaving the leader, but one exists in game")

    r = requests.post("http://" + serverIP + ":8080/palermo/api/v1/user/logIn",
                      json={"username": username, "password": password})
    if r.status_code != 200: sys.exit("Error with logging out")
    userID1 = r.json()
    r = requests.post("http://" + serverIP + ":8080/palermo/api/v1/game/joinGame/" + gameID, json=userID1)
    printin(r.status_code == 200 and r.json()['leader_id']==userID2['user_id'],
            "The game leader has changed and now is the one that stayed in the game")


    r = requests.post("http://" + serverIP + ":8080/palermo/api/v1/user/logOut", json=userID1)
    if r.status_code != 200: sys.exit("Error with clearing data")

    r = requests.post("http://" + serverIP + ":8080/palermo/api/v1/user/logOut", json=userID2)
    if r.status_code != 200: sys.exit("Error with clearing data")

    print("Clearing data ... OK")
