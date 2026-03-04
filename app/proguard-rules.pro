# Add project specific ProGuard rules here.
# By default, the rules in this file are appended at the end of the bundled
# ProGuard rules from the Android Gradle plugin.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# Room rules
-keepclassmembers class * extends androidx.room.RoomDatabase {
    <init>(...);
}

# Keep the entity classes
-keep class com.paddez.wordformed.HiScore { *; }

# Keep the DAO interfaces
-keep interface com.paddez.wordformed.HiScoreDao { *; }

# Keep the ViewModels
-keep class * extends androidx.lifecycle.ViewModel { *; }
