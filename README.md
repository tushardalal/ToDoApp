# ToDoApp

**ToDoApp** is Simple android TODO Task App. This supports the Create Task/ Edit Task & Delete Task functionality. It also supports the following features like task Due, task Priority as well as status whether it is Done or Pending.


**NEW FEATURE**

**New feature is added to Create the task by speaking the task with day and priority.**

For example:
You can create the new task by saying 'Pickup books tomorrow' - Will type the text into the Task Description and set the due date tomorrow.
OR
You can say 'Pickup books next Friday high priority' - Will type the text into Task Description and set the due date next Friday with High priority preset in create/edit task screen.


Submitted by: Tushar Dalal

Time spent: 32 hours spent in total (including setup / image editing)

## User Stories

**REQUIRED** functionality is completed:

* [x] User can **successfully add and remove items** from the todo list
* [x] User can **tap a todo item in the list and bring up an edit screen for the TODO item** and then have any changes to the text reflected in the todo list.
* [x] User can **persist todo items** and retrieve them properly on app restart


**OPTIONAL** functionality

* [x] Persist the todo items [into SQLite](http://guides.codepath.com/android/Persisting-Data-to-the-Device#sqlite) instead of a text file
* [x] Improve style of the todo items in the list [using a custom adapter](http://guides.codepath.com/android/Using-an-ArrayAdapter-with-ListView)
* [x] Add support for completion due dates for todo items (and display within listview item)
* [ ] Use a [DialogFragment](http://guides.codepath.com/android/Using-DialogFragment) instead of new Activity for editing items.
* [x] Add support for selecting the priority of each todo item (and display in listview item)
* [x] Tweak the style improving the UI / UX, play with colors, images or backgrounds

The following **additional** features are implemented:
* [x] Delete Task introduced with comfirmation message.
* [x] Use a [DialogFragment] to show the Date capture dialog.
* [x] Splash Screen with App Logo
* [x] Added 'Save' and 'Delete' icons in Create/Edit Task Activity.

Next **Release** features.

* [ ] Planning to add hands free mode to read all task one by one.
* [ ] Planning to add App Settings with optiosn like
      Purge completed task
      Sort Task based on Priority - or natural order.  



Video Walkthrough 

Here's a walkthrough of implemented user stories:

<img src='https://github.com/tushardalal/ToDoApp/blob/master/ToDoApp_3.gif' title='Video Walkthrough' width='' alt='Video Walkthrough' />

GIF created with [LiceCap](http://www.cockos.com/licecap/).

## Notes

Challange was to build custom list view line item display.

## License

    Copyright [2016] [Tushar Dalal]

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

        http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.