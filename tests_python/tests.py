from TestCase1 import testCase1
from TestCase2 import testcase2
from TestCase3 import testCase3
from TestCase4 import testCase4
from TestCreateUser import testCreateUser, deletingUser
from TestLogIn import testLogIn

serverIP = "127.0.0.1"
username1 = "test2"
password = "1234"
username2 = "test3"
username3 = "test4"

testCreateUser(serverIP, username1, password)
userToken = testLogIn(serverIP, username1, password)
deletingUser(serverIP, username1, password)

testCase1(serverIP, username1, password)
deletingUser(serverIP, username1, password)

testcase2(serverIP, username1, password, username2, password)
deletingUser(serverIP, username1, password)
deletingUser(serverIP, username2, password)

testCase3(serverIP, username1, password, username2, password)
deletingUser(serverIP, username1, password)
deletingUser(serverIP, username2, password)

testCase4(serverIP, username1, password, username2, password, username3, password)
deletingUser(serverIP, username1, password)
deletingUser(serverIP, username2, password)
deletingUser(serverIP, username3, password)


