# Reminder App Play

For my bachelor's thesis I'm creating a 12factor app using different web frameworks which run on heroku. This is the Play framework version using Scala

## The App

The app is a simple reminder app. It features:

- An API where users can post a remind message, a due date and their email
- Background workers which check the reminders given by all users and send emails to the users when they wanted to be reminded
- Admin tasks like deleting all reminders or sending all reminders right now

The app should implement all 12 factors of the 12 factor app methodology: https://12factor.net
