-keep,includedescriptorclasses class br.com.programadorthi.facts.remote.raw.**$$serializer { *; }
-keepclassmembers class br.com.programadorthi.facts.remote.raw.** {
    *** Companion;
}
-keepclasseswithmembers class br.com.programadorthi.facts.remote.raw.** {
    kotlinx.serialization.KSerializer serializer(...);
}