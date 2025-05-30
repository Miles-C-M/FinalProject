![image](https://github.com/user-attachments/assets/1c44f81e-6158-4857-b094-1e9ab9b16de4)

🎵 Music Favorites App
A retro-inspired Android app that allows users to search for tracks, mark their favorites, and explore concert events — all while embracing a playful, forum-style aesthetic.

✨ Features
✅ Search & Explore

Search a vast library of tracks via the Last.fm API

View detailed track information, including album artwork

✅ Event Discovery

Find live concerts and events via the Ticketmaster API

✅ Personalized Experience

Firebase Authentication with Auth UI for secure login

Save favorite songs to a personal list using Firestore Database

✅ Modern UI/UX

Bottom navigation with three intuitive fragments

Multiple RecyclerViews with custom adapters and layouts

Retro-futurist aesthetic with a modern meme-inspired logo

✅ Device Interaction

Integrates accelerometer for unique interactive features

🚀 Installation
Clone the repository:
https://github.com/Miles-C-M/FinalProject

Open the project in Android Studio.

Add your API keys:

Ticketmaster API Key

Last.fm API Key

Firebase Project Configuration

Build and run on your emulator or device.

🛠️ Tech Stack
Languages: Kotlin

APIs: Last.fm API, Ticketmaster API

Backend: Firebase Authentication, Firestore Database

Architecture: MVVM

UI: Jetpack Compose, RecyclerViews, BottomNavigationView

Device Sensors: Android Accelerometer

🏗️ Development Notes
Challenges Encountered
Firebase Authentication: Initial version conflicts resolved via lib.versions.toml.

Last.fm API Quirk: Album artwork not reliably returned in track search API. Resolved by making an additional track.getInfo call to fetch correct album art.

Design Inspiration
Styled after classic internet music forums with a modern, sarcastic twist — aiming to appeal to both retro and Gen Z users.
