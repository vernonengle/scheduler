# scheduler
Task Scheduler

Sample input:
~~~~{
  "scheduleStart": "2019-02-01",
  "tasks": [
    {
      "id": 1,
      "name": "Task A",
      "duration": 6,
      "dependencies": []
    },
    {
      "id": 2,
      "name": "Task B",
      "duration": 3,
      "dependencies": []
    },
    {
      "id": 3,
      "name": "Task C",
      "duration": 5,
      "dependencies": [
        2
      ]
    },
    {
      "id": 4,
      "name": "Task D",
      "duration": 3,
      "dependencies": [
        1,
        3
      ]
    }
  ]
}
~~~~

Run jar with 1st argument as the path to the schedule file
~~~~
java -jar scheduling-1.0-SNAPSHOT.jar /path/to/file/TaskList.json
~~~~

Sample output:
~~~~
========Start of Schedule 1==============
Task: 1
   ID        : Task A
   Start Date: 2019-02-01
   End Date  : 2019-02-07

Task: 2
   ID        : Task B
   Start Date: 2019-02-01
   End Date  : 2019-02-04

Task: 3
   ID        : Task C
   Start Date: 2019-02-04
   End Date  : 2019-02-09

Task: 4
   ID        : Task D
   Start Date: 2019-02-09
   End Date  : 2019-02-12

========End of Schedule 1================
~~~~~

If more than one schedule is possible, those will be shown as well.

