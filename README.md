# ***Library-Management-System***
LibManager is a web-based application designed to streamline the management of library resources,
patrons, and transactions. It provides librarians and administrators with a centralized platform 
to efficiently manage library operations and services.

# ***DESCRIPTION***
LibManager is the first version of my Library Management System application.
While it offers essential functionalities for managing library resources, including user authentication, 
borrowing, and admin features, we recognize that there is room for improvement. 
We are committed to continuously enhancing the application based on user feedback and emerging requirements.

# ***MAIN TOOLS/FRAMEWORKS*** 
:::Spring Boot: Provides a powerful framework for building web applications with ease.
:::PostgreSQL: Used for data storage to ensure reliable and scalable database management.
:::Mockito and JUnit: Utilized for testing to ensure the reliability and accuracy of the application.


# ***FEATURES***
The main features of LibManager include -

:::Security: The application prioritizes security and utilizes Spring Security for authentication and authorization.
:::User Authentication: Users can securely sign up for new accounts and log in.
:::Borrowing: Patrons can borrow any available book in the system.
:::Admin Features: Administrators have access to additional functionalities, including managing book details, borrow records, adding new books, updating book details, and removing books from the library.
:::Tracking: Borrow records and patron details are tracked to monitor the borrowing status of each patron.

# ***VERSION***
This is the first version of LibManager, and I am committed to ongoing improvements and enhancements.
Future versions may include additional features, optimizations, and bug fixes to further enhance the functionality 
and user experience of the system.

# ***FEEDBACK AND CONTRIBUTIONS***
I welcome any feedback or suggestions you may have regarding LibManager. If you encounter any issues 
or have ideas for improvement, please feel free to open an issue or submit a pull request on my GitHub repository. 
Your contributions are invaluable in shaping the future of this application.

# ***PROJECT STRUCTURE***

LibraryManagementSystem/
├── .idea/
├── .mvn/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── libManager/
│   │   │       ├── config/
│   │   │       │   ├── JwtAuthenticationFilter.java
│   │   │       │   ├── LogoutConfiguration.java
│   │   │       │   ├── SecurityConfiguration.java
│   │   │       │   ├── SecurityFilterConfiguration.java
│   │   │       │   └── SwaggerConfiguration.java
│   │   │       ├── controller/
│   │   │       │   ├── AuthController.java
│   │   │       │   ├── BookController.java
│   │   │       │   ├── BorrowController.java
│   │   │       │   └── PatronController.java
│   │   │       ├── dto/
│   │   │       │   ├── BookRequest.java
│   │   │       │   ├── LoginDto.java
│   │   │       │   ├── SignupDto.java
│   │   │       │   └── UserRequest.java
│   │   │       ├── entity/
│   │   │       │   ├── BaseEntity.java
│   │   │       │   ├── Book.java
│   │   │       │   ├── BookRecord.java
│   │   │       │   └── User.java
│   │   │       ├── enums/
│   │   │       │   └── Role.java
│   │   │       ├── exception/
│   │   │       │   ├── LibManagerException.java
│   │   │       │   └── LibManagerExceptionHandler.java
│   │   │       ├── mapper/
│   │   │       │   ├── BookMapper.java
│   │   │       │   └── UserMapper.java
│   │   │       ├── payload/
│   │   │       │   ├── ApiResponse.java
│   │   │       │   ├── BookData.java
│   │   │       │   ├── BookDetail.java
│   │   │       │   ├── LoginResponse.java
│   │   │       │   ├── PatronDetail.java
│   │   │       │   └── UserData.java
│   │   │       ├── repository/
│   │   │       │   ├── BookRepository.java
│   │   │       │   ├── RecordRepository.java
│   │   │       │   └── UserRepository.java
│   │   │       ├── service/
│   │   │       │   ├── serviceImplementation/
│   │   │       │   │   ├── AuthImplementation.java
│   │   │       │   │   ├── BookServiceImplementation.java
│   │   │       │   │   ├── BorrowingServiceImplementation.java
│   │   │       │   │   ├── JwtImplementation.java
│   │   │       │   │   ├── PatronServiceImplementation.java
│   │   │       │   │   └── AuthenticationService.java
│   │   │       │   ├── BookService.java
│   │   │       │   ├── BorrowingService.java
│   │   │       │   ├── JwtService.java
│   │   │       │   └── PatronService.java
│   │   │       └── util/
│   │   │           └── UserUtil.java
│   │   └── resources/
│   │       ├── static/
│   │       └── templates/
│   ├── test/
│   │   ├── java/
│   │   │   └── libManager/
│   │   │       ├── controller/
│   │   │       │   ├── AuthControllerTest.java
│   │   │       │   ├── BookControllerTest.java
│   │   │       │   ├── BorrowControllerTest.java
│   │   │       │   └── PatronControllerTest.java
│   │   │       └── serviceImplementation/
│   │   │           ├── AuthImplementationTest.java
│   │   │           ├── BookServiceImplementationTest.java
│   │   │           ├── BorrowingServiceImplementationTest.java
│   │   │           └── PatronServiceImplementationTest.java
│   └── resources/
│       └── application.properties
├── target/
├── .gitignore
├── HELP.md
├── mvnw
├── mvnw.cmd
├── pom.xml
└── README.md