# Usecases

## Introduction

This document describes all usecases of the Trip Maester. Here is the list of Usecases:

ID   | Name 
-----|-----
UC01 | Login


## Details

### Authentication

#### UC01.Login

| | |
|-|-|
| Actors            | User 
| Preconditions     | User is not logined 
| Basic flows       | 1. User open left navigation menu. 
|                   | 2. User tap on Login button.
|                   | 3. Show message that user login via Facebook
|                   | 4. User authorizes Facebook to login
|                   | 5. User logined. Profile picture is displayed on navigation menu |
| Alternative flows | 4.a. User does not grant privilege permission
|                   |  4.a.1 Display message show error to user 
| Alternative flows | 5.a. User does not have account yet
|                   | 5.a.1 Create an account for user
|                   | 5.a.2 User logined. Profile picture is displayed on navigation menu 
