import sys

import requests

from printing import printin


def testChangeState(serverIP, userID1):
    '''

    :param serverIP:
    :param username:
    :param password:
    :param username2:
    :param password2:
    :param username3:
    :param password3:
    :return:
    '''

    print("Testing change states about Roles")

    # Testing dead players
    r = requests.post("http://" + serverIP + ":8080/palermo/api/v1/ingame/deadPlayers", json=userID1)
    printin(r.status_code==200,"Get dead players")

    # Testing all roles
    r = requests.post("http://" + serverIP + ":8080/palermo/api/v1/ingame/allRoles", json=userID1)
    printin(r.status_code == 200, "Get all roles")

    # Testing dead roles
    r = requests.post("http://" + serverIP + ":8080/palermo/api/v1/ingame/deadRoles", json=userID1)
    printin(r.status_code == 200, "Get dead roles")
