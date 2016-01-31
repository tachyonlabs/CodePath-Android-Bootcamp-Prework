# Pre-work - Simple Todo

Simple Todo is an android app that allows building a todo list and basic todo items management functionality including adding new items, editing and deleting an existing item.

Submitted by: Tané Tachyon

Time spent: Hard to say -- for one thing, I already had Android Studio installed, and for another, I did different bits (doing your video walkthroughs and so on) on different days, and was just enjoying myself without timing myself.

## User Stories

The following **required** functionality is completed:

* [x] User can **successfully add and remove items** from the todo list
* [x] User can **tap a todo item in the list and bring up an edit screen for the todo item** and then have any changes to the text reflected in the todo list.
* [x] User can **persist todo items** and retrieve them properly on app restart

The following **optional** features are implemented:

* [x] Persist the todo items [into SQLite](http://guides.codepath.com/android/Persisting-Data-to-the-Device#sqlite) instead of a text file
* [x] Improve style of the todo items in the list [using a custom adapter](http://guides.codepath.com/android/Using-an-ArrayAdapter-with-ListView)
* [x] Add support for completion due dates for todo items (and display within listview item)

The following **additional** features are implemented:

* [x] Checkboxes! User can **check off (and delete) an item by checking its checkbox**
* [x] User can **tap a floating action button to add an item -- less onscreen clutter**
* [x] Adding and editing items now both use EditItemActivity for consistency and, again, less clutter
* [x] User gets one of many **congratulations toasts when checking off an item**
* [x] User can **enter items with the first letter capitalized without having to tap shift first**

## Video Walkthrough 

Here's a walkthrough of implemented user stories:

<img src='http://tachyonlabs.com/miscimages/simple_todo3.gif' title='Video Walkthrough' width='' alt='Video Walkthrough' />

GIF created with [LiceCap](http://www.cockos.com/licecap/).

## Notes

The next step will be to add a DatePicker instead of using an EditText to enter/edit dates, but I was at a stopping point and wanted to upload my changes.

## License

    Copyright 2016 Tané Tachyon

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

        http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
