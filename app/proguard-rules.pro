# ProGuard Configuration file
#
# See http://proguard.sourceforge.net/index.html#manual/usage.html

# SerializedName Annotation
-keepclassmembers,allowobfuscation class * {
    @com.google.gson.annotations.SerializedName <fields>;
}
