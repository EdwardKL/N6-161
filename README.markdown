# About
This is the repository for Team Noble Six's 161 project.

# Project Milestones
* March 9 - Finish background study of the solution we want to implement, have a concrete idea of the functionality of our app, and understand Android permissions and OS.
* March 16 - Learn the basics of how to program / create an Android app.
* March 21 - Create a working app that can list all the other apps on the phone.
* March 28 - Link up the app with the database. Create a list of policies that can be applied or taken away from apps. This will include:
    * Internet policy
        * The user can specify which URLs the app can communicate with through a regular expression
    * File system
        * The user can specify which files the app can read through a regular expression
        * Similar policies will apply for write and execute
    * Phone information
        * The user can specify what information the app may access, such as serial number, phone number, etc.
    * SMS/Phone pictures/etc.
        * This is assumed to be taken care of with file system policies
    * GPS/Camera/miscellaneous phone features
        * The user can specify which phone features the app may use
* April 5 - Create a hierarchy of policy guidelines. The most general of guidelines will be applied to any app whose policies have not been specifically modified through our app. This is to simplify the user’s interaction with our app.
* April 12 - Have the OS consult our apps database whenever another app wants to do something that requires external resources.
* April 19 - Through an iterative process of implementation and experimentation, have the app prototype almost completely working.
* April 26 - app ready for final evaluation.
* May 3rd - Final changes made after evaluation, app complete.
* May 4th - Report completed.



# Measures of Success

* Our app should be able to completely list all other apps on the phone and have these apps linked to a database that lists each app’s specific security policy.
* The OS should consult our database whenever an app asks for an external resource.
* The OS should comply perfectly with the policies listed in our database.
* The user should be able to make changes to the database through our app.
