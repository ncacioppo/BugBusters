Once you run main, the terminal will run. It looks like this: Grove City College Schedule Maker ->

On this section there are 5 possible commands:

HELP -> Prints out instructions on how to use the current section of the terminal

EXIT -> Closes the application

Search -> Takes you to a new section where you have a new set of commands for editing and creating schedules.

Schedule -> Takes you to a new section where you have new commands for searching for courses and adding them to schedules.

User -> Prints out all of the user info the app has and also allows for set up if it has not been setup yet.
NOTE** User can also take arguments:
User Major <majorname> <requirementsyear> -> Adds the specified major with the specified requirements year to the user
User Minor <minorname> <requirementsyear> -> Adds the specified minor with the specified requirements year to the user
User Remove Major -> Takes you to a section to choose a major to be removed from user account
User Remove Minor ->Takes you to a section to choose a major to be removed from user account


SCHEDULE section:
In this section there are a new set of commands:

All -> Prints out all the schedules for a user

Back -> Moves back to the previous section of the terminal

Help -> Prints out instruction on how to use this section of the terminal

Create <schedulename> <season> <year> -> Creates a schedule for the user with specified name and term (season & year)

Delete -> Takes you to a section to choose which schedule to delete

View <schedulename> <viewtype> -> Lets you view a specified schedule in 1 of 2 viewtypes; either list or calendar

Add <schedulename> <courseid> -> Adds specified course to specified schedule

Remove <schedulename> <courseid> -> Removes specified course from specified schedule



SEARCH section:
In this section there are a new set of commands:

Back -> Takes you back to the previous section of the terminal

Clear -> This removes all filters added to course and changes the current list of courses to be all courses

Courses -> Prints out the current list of courses

Add <schedulename> <courseid> -> adds specified course to specified schedule

<filter> <query> -> This refines the current list of courses based on the given filter and query
NOTE** Possible filters are:
Keyword, Name, Department, Code, Term, Professor, ID, Day, Time

Most queries take the format of a normal string Here are the few that need a specific format:
CODE <query> -> query should be 'start-end' where start and end are integers and are both inclusive
TIME <query> -> query should be 'startTime-endTime' where start and end are both inclusive and 24h time. Ex. '09:00-15:00'
TERM <query> -> query should be 'season-year' ex. spring-2020
DAY <query> -> query should be the written out name of th eday and filter has to be applied one day at a time. (Note each day is 'anded' to teh previous ones)

