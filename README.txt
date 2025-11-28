STUDENT INFORMATION:
Name: Trần Hoàng Minh
Student ID: ITCSIU23025
Class: Web App Dev Lab 02

Note: Screenshots are already in report file

COMPLETED EXERCISES:
[x] Exercise 1: Database & User Model
[x] Exercise 2: User Model & DAO
[x] Exercise 3: Login/Logout Controllers
[x] Exercise 4: Views & Dashboard
[x] Exercise 5: Authentication Filter
[x] Exercise 6: Admin Authorization Filter
[x] Exercise 7: Role-Based UI
[x] Exercise 8: Change Password

AUTHENTICATION COMPONENTS:
- Models: User.java
- DAOs: UserDAO.java
- Controllers: LoginController.java, LogoutController.java, DashboardController.java
- Filters: AuthFilter.java, AdminFilter.java
- Views: login.jsp, dashboard.jsp, updated student-list.jsp

TEST CREDENTIALS:
Admin:
- Username: admin
- Password: password123

Regular User:
- Username: john
- Password: password123

FEATURES IMPLEMENTED:
- User authentication with BCrypt
- Session management
- Login/Logout functionality
- Dashboard with statistics
- Authentication filter for protected pages
- Admin authorization filter
- Role-based UI elements
- Password security

SECURITY MEASURES:
- BCrypt password hashing
- Session regeneration after login
- Session timeout (30 minutes)
- SQL injection prevention (PreparedStatement)
- Input validation
- XSS prevention (JSTL escaping)

KNOWN ISSUES:
- The search button in dashboard does not work because there's no search term when pressing it.


BONUS FEATURES:
- None

TIME SPENT: [10hrs]

TESTING NOTES:
[Describe how you tested authentication, filters, and authorization]
Place println() statements and read the server output on the console.