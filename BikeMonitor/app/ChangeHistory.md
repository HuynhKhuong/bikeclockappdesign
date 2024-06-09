History Change:

--> Start from Jun 8 - by Tri, Changing content:

1. Adding neccessary final variable, library for login activity.
2. Adding VerifyDataWithFirebase() method, scope:
-   Get data from firebase to verify Email, PW from TextView.
-   Alert when detecting wrong email, wrong pw, missed email/pw.
-   Call callback function to setup user data to UserInfor.
3. Adding onLoginStatusChanged callback function, scope:
-   Called when VerifyDataWithFirebase() retrieve requested data from Firebase.
-   Setup user data.
-   Navigate to other view if successfully log-in.
4. Adding m_logInSuccessSts in UserInfor Class for future usage.

*Note: Navigation activity must be implemented in callback function, when data from Firebase already successfully got. Firebase service tasks work asynchronous with LogInFragment task.

--> End
