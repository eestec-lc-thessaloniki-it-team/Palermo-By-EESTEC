import requests
from printing import printin



def testCase1(serverIP,username,password):
    '''
    This  will demostrate a user that creates an account and then leaves the app
    :return: Nothing
    '''
    print("\nTest case #1 Create Acc and leave the App \n")

    r = requests.post("http://" + serverIP + ":8080/palermo/api/v1/user/newUser",
                      json={"username": "tester", "password": password})
    printin(r.status_code == 406, "Adding user with already existed username")

    r = requests.post("http://" + serverIP + ":8080/palermo/api/v1/user/newUser",
                      json={"username": username, "password": password})
    printin(r.status_code == 200, "Adding correct user")
    userID = r.json()

    r = requests.post("http://" + serverIP + ":8080/palermo/api/v1/user/logOut", json=userID)
    printin(r.status_code == 200, "Log out")