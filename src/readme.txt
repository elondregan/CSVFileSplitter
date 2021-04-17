Details on the program:

I built this program on Jdk 16, so it required Gradle version 7+ to run correctly, hopefully
the gradle wrapper attached to the program will let the application run without needing to locally
set it up.

To run:
Execute the following command:
./gradlew clean build run

Current flaws with the program:
Initially, this problem seems pretty simple, we need to group data, de-dupe it and sort it.

The devil is of course in the details, sorting the code as we write the data, is nasty, we can correctly place the
data, but then if we need to place it earlier, it becomes horrible in that we need to shift the other records.

So, the logical solution here is that we just sort in memory which is NLogN, but what if we have a huge CSV file?

The bad news is a company like walmart has 2.2 million employees, and sending all that data would bring my application
to its knees and would require a much more robust system and at that point, I would recommend using a SQL database
and then writing records from said application through a pagination process.

I am operating under the assumption that we hopefully are not receiving enough to run out of memory, and we can hold
all our records in memory, which honestly makes the problem significantly easier and may be an unfair assumption to make.

Currently the flow is: dedupe -> sort -> group -> write to the various CSVs.
