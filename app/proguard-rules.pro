# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in /home/appinventiv/Android/Sdk/tools/proguard/proguard-android.txt
# You can edit the include path and order by changing the proguardFiles
# directive in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# Add any project specific keep options here:

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

# Uncomment this to preserve the line number information for
# debugging stack traces.
#-keepattributes SourceFile,LineNumberTable

# If you keep the line number information, uncomment this to
# hide the original source file name.
#-renamesourcefileattribute SourceFile


-keep public class * implements com.bumptech.glide.module.GlideModule
-keep public class * extends com.bumptech.glide.module.AppGlideModule
-keep public enum com.bumptech.glide.load.resource.bitmap.ImageHeaderParser$** {
  **[] $VALUES;
  public *;
}

-dontoptimize
-keepattributes SourceFile,LineNumberTable
-keepattributes Annotation
#-keepclassmembers class my.package { !private ; protected ; public ; ; }
-keep class org.acra.** {*;}
#-keepclasseswithmembernames class * {
# native ;
# }
#-keepclassmembers public class * extends android.view.View {#      void set*(***);#      * get*();#    }
-dontwarn com.android.support.**
-dontwarn org.w3c.dom.bootstrap.DOMImplementationRegistry
-keepattributes *Annotation*,EnclosingMethod,Signature
-keepnames class com.fasterxml.jackson.** { *; }
-dontwarn com.fasterxml.jackson.databind.**
# -keep class org.codehaus.** { *; }
#-keepclassmembers public final enum org.codehaus.jackson.annotate.JsonAutoDetect$Visibility {
# public static final org.codehaus.jackson.annotate.JsonAutoDetect$Visibility *; }
-keep public class your.class.** {  public void set*(***);  public *** get*();}
-dontwarn com.github.siyamed.**
-keep class com.github.siyamed.shapeimageview.**{ *; }
-dontwarn okio.**
-dontwarn retrofit2.Platform$Java8
-keep class retrofit.** { *; }
-keepclasseswithmembers class * {    @retrofit.** *;}
-keepclassmembers class * {    @retrofit.** *;}
# Proguard configuration for Jackson 2.x (fasterxml package instead of codehaus package)

-dontwarn java.lang.ClassValue

-dontwarn com.google.**
-keep class com.google.** { *; }
-dontwarn java.lang.ClassValue
-keep class java.lang.ClassValue { *; }
-dontwarn org.codehaus.mojo.animal_sniffer.IgnoreJRERequirement
-keep class org.codehaus.mojo.animal_sniffer.IgnoreJRERequirement { *; }

