# Pre-work - *ToDoList*

**ToDoList** is an android app that allows building a todo list and basic todo items management functionality including adding new items, editing and deleting an existing item.

Submitted by: **Joanna Illing**

Time spent: **38** hours spent in total

## User Stories

The following **required** functionality is completed:

* [X] User can **successfully add and remove items** from the todo list
* [X] User can **tap a todo item in the list and bring up an edit screen for the todo item** and then have any changes to the text reflected in the todo list.
* [X] User can **persist todo items** and retrieve them properly on app restart

The following **optional** features are implemented:

* [X] Persist the todo items [into SQLite](http://guides.codepath.com/android/Persisting-Data-to-the-Device#sqlite) instead of a text file
* [X] Improve style of the todo items in the list [using a custom adapter](http://guides.codepath.com/android/Using-an-ArrayAdapter-with-ListView)
* [X] Add support for completion due dates for todo items (and display within listview item)
* [ ] Use a [DialogFragment](http://guides.codepath.com/android/Using-DialogFragment) instead of new Activity for editing items
* [ ] Add support for selecting the priority of each todo item (and display in listview item)
* [X] Tweak the style improving the UI / UX, play with colors, images or backgrounds

The following **additional** features are implemented:

* [ ] List anything else that you can get done to improve the app functionality!

## Video Walkthrough 

Here's a walkthrough of implemented user stories:

<img src='http://i.imgur.com/jwg42nF.gif' title='Video Walkthrough' width='' alt='Video Walkthrough' />

GIF created with [Maarten Baert's SimpleScreenRecorder](http://www.maartenbaert.be/simplescreenrecorder/) and 
[MakeAGif.com](http://makeagif.com/video-to-gif).

## Notes

This app was challenging to build but I learned a lot. I initially used a global variable for 
position in the main java activity, but switched to passing the position via an Intent along 
with the item text as a String. I am curious as to what the best practices dictate for this type
of situation.

I am unsure about the icon in the action bar as there were [two ways](https://guides.codepath.com/android/Defining-The-ActionBar#changing-the-actionbar-icon-or-title) to implement this on the codepath
guides - via the java code and via XML in the AndroidManifest file. I was not able to get this to 
work without adding the java. I am particularly curious about the middle line of java code because
it seems redundant to the XML since the icon is set twice. I intend to learn more about this.

Another issue: I am using Linux (Ubuntu) as my primary OS and the LiceCap GIF creator appears to
be for Windows and OSX only. I found an alternative screen recorder for Linux [here](http://www.maartenbaert.be/simplescreenrecorder/) which recorded
a video in .mp4 format. I used [MakeAGif.com](http://makeagif.com/video-to-gif) to convert the .mp4 file to a .gif file and then uploaded the .gif to [imgur](http://i.imgur.com/jwg42nF.gif).

I noticed that the Genymotion emulator does not display the on-screen keyboard. In the future, I would like
to show the onscreen keyboard because it affects the screen layouts for both the main activity and the 
edit activity. I used my Android phone to test the app to ensure that the Add Item and Save buttons are still
visible when the on-screen keyboard is active.


## License

    Copyright 2015 Joanna Illing

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

        http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
