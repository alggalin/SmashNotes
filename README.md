# SmashNotes

This app can be utilized to keep track of matchup knowledge in Smash Ultimate, written using Kotlin, Jetpack Compose and a MVVM design.

## Screenshots
<img width="354" alt="Capture3" src="https://github.com/user-attachments/assets/400636bd-64f1-4cbf-909e-acac83bd64e9">

<img width="350" alt="Capture" src="https://github.com/user-attachments/assets/b56b05ce-3662-4e76-b47e-f652a3340b0b">

<img width="347" alt="Capture2" src="https://github.com/user-attachments/assets/5e9bc02f-3442-425f-840a-899a18c458bb">

## Features
Create note categories for specific matchups.

Within those categories create notes for the matchups.

Rearrange the order of notes at any time.

Add relevant links to the matchups that are able to be open on the default browser/app once selected.

## How to use

A matchup must initially be selected in order to begin creating categories or adding links.

Once categories are created you simply tap on your desired category to begin adding notes to that specific matchup.

Links saved will open in the browser upon tapping. The link must be a valid link or it will fail to open. To clarify, the link itself need to be added under the "Link Text" textfield.

## Libraries Used
* [Kotlin Coroutines](https://kotlinlang.org/docs/coroutines-overview.html)
* [Jetpack Compose](https://developer.android.com/compose)
* [Navigation](https://developer.android.com/guide/navigation)
* [Reorderable](https://github.com/Calvin-LL/Reorderable?tab=readme-ov-file)
* [Room Database](https://developer.android.com/reference/androidx/room/package-summary)
* [ViewModel](https://developer.android.com/topic/libraries/architecture/viewmodel)
