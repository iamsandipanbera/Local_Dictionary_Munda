-keepattributes SourceFile, LineNumberTable

-renamesourcefileattribute SourceFile
-repackageclasses

-ignorewarnings
-dontwarn
-dontnote

-dontwarn android.arch.**
-dontwarn android.lifecycle.**
-keep class android.arch.** { *; }
-keep class android.lifecycle.** { *; }

-dontwarn androidx.arch.**
-dontwarn androidx.lifecycle.**
-keep class androidx.arch.** { *; }
-keep class androidx.lifecycle.** { *; }
-DevSan
