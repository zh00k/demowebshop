# Demo Web Shop test automation

## Booking API tests

Before running the Booking API tests, create `src/test/resources/config/booking.properties`:

```properties
bookingUrl=https://restful-booker.herokuapp.com
username=YOUR_USERNAME
password=YOUR_PASSWORD
```

Replace the placeholders with your local values. The properties file is excluded from Git; do not commit it.

Run the API tests with:

```bash
./gradlew apiTest
```
