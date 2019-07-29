from TestCreateUser import testCreateUser, deletingUser
from TestLogIn import testLogIn

testCreateUser("127.0.0.1", "test2", "1234")
userToken = testLogIn("127.0.0.1", "test2", "1234")
deletingUser("127.0.0.1", userToken)
