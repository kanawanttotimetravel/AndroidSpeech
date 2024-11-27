# AndroidSpeech
The main file is `app/src/main/java/com/example/myapplication/MainActivity.java`

## Functions
The 2 main functions are `speechToTextFunction()` and `textToSpeechFunction()`.

- `speechToTextFunction()` invoke a `startActivityForResult()`, whose results can only be retrieved through invoking `onActivityResult()` due to Android magic (or I'm just dumb).

- `textToSpeechFunction(String text)` is more straightforward, it directly speask whatever the input `text` is.
