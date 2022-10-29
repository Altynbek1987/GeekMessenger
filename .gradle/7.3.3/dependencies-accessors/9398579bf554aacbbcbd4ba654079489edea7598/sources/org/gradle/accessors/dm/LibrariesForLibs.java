package org.gradle.accessors.dm;

import org.gradle.api.NonNullApi;
import org.gradle.api.artifacts.MinimalExternalModuleDependency;
import org.gradle.plugin.use.PluginDependency;
import org.gradle.api.artifacts.ExternalModuleDependencyBundle;
import org.gradle.api.artifacts.MutableVersionConstraint;
import org.gradle.api.provider.Provider;
import org.gradle.api.provider.ProviderFactory;
import org.gradle.api.internal.catalog.AbstractExternalDependencyFactory;
import org.gradle.api.internal.catalog.DefaultVersionCatalog;
import java.util.Map;
import javax.inject.Inject;

/**
 * A catalog of dependencies accessible via the `libs` extension.
*/
@NonNullApi
public class LibrariesForLibs extends AbstractExternalDependencyFactory {

    private final AbstractExternalDependencyFactory owner = this;
    private final AlgoliaLibraryAccessors laccForAlgoliaLibraryAccessors = new AlgoliaLibraryAccessors(owner);
    private final AndroidLibraryAccessors laccForAndroidLibraryAccessors = new AndroidLibraryAccessors(owner);
    private final AvatarViewLibraryAccessors laccForAvatarViewLibraryAccessors = new AvatarViewLibraryAccessors(owner);
    private final CoroutinesLibraryAccessors laccForCoroutinesLibraryAccessors = new CoroutinesLibraryAccessors(owner);
    private final DatastoreLibraryAccessors laccForDatastoreLibraryAccessors = new DatastoreLibraryAccessors(owner);
    private final FirebaseLibraryAccessors laccForFirebaseLibraryAccessors = new FirebaseLibraryAccessors(owner);
    private final GlideLibraryAccessors laccForGlideLibraryAccessors = new GlideLibraryAccessors(owner);
    private final HiltLibraryAccessors laccForHiltLibraryAccessors = new HiltLibraryAccessors(owner);
    private final JavaxLibraryAccessors laccForJavaxLibraryAccessors = new JavaxLibraryAccessors(owner);
    private final KoinLibraryAccessors laccForKoinLibraryAccessors = new KoinLibraryAccessors(owner);
    private final KotlinLibraryAccessors laccForKotlinLibraryAccessors = new KotlinLibraryAccessors(owner);
    private final KropLibraryAccessors laccForKropLibraryAccessors = new KropLibraryAccessors(owner);
    private final LegacySupportLibraryAccessors laccForLegacySupportLibraryAccessors = new LegacySupportLibraryAccessors(owner);
    private final LifecycleLibraryAccessors laccForLifecycleLibraryAccessors = new LifecycleLibraryAccessors(owner);
    private final NavigationLibraryAccessors laccForNavigationLibraryAccessors = new NavigationLibraryAccessors(owner);
    private final OkHttpLibraryAccessors laccForOkHttpLibraryAccessors = new OkHttpLibraryAccessors(owner);
    private final PagingLibraryAccessors laccForPagingLibraryAccessors = new PagingLibraryAccessors(owner);
    private final PicassoLibraryAccessors laccForPicassoLibraryAccessors = new PicassoLibraryAccessors(owner);
    private final RetrofitLibraryAccessors laccForRetrofitLibraryAccessors = new RetrofitLibraryAccessors(owner);
    private final RoomLibraryAccessors laccForRoomLibraryAccessors = new RoomLibraryAccessors(owner);
    private final UiLibraryAccessors laccForUiLibraryAccessors = new UiLibraryAccessors(owner);
    private final VersionAccessors vaccForVersionAccessors = new VersionAccessors(providers, config);
    private final BundleAccessors baccForBundleAccessors = new BundleAccessors(providers, config);
    private final PluginAccessors paccForPluginAccessors = new PluginAccessors(providers, config);

    @Inject
    public LibrariesForLibs(DefaultVersionCatalog config, ProviderFactory providers) {
        super(config, providers);
    }

    /**
     * Returns the group of libraries at algolia
     */
    public AlgoliaLibraryAccessors getAlgolia() { return laccForAlgoliaLibraryAccessors; }

    /**
     * Returns the group of libraries at android
     */
    public AndroidLibraryAccessors getAndroid() { return laccForAndroidLibraryAccessors; }

    /**
     * Returns the group of libraries at avatarView
     */
    public AvatarViewLibraryAccessors getAvatarView() { return laccForAvatarViewLibraryAccessors; }

    /**
     * Returns the group of libraries at coroutines
     */
    public CoroutinesLibraryAccessors getCoroutines() { return laccForCoroutinesLibraryAccessors; }

    /**
     * Returns the group of libraries at datastore
     */
    public DatastoreLibraryAccessors getDatastore() { return laccForDatastoreLibraryAccessors; }

    /**
     * Returns the group of libraries at firebase
     */
    public FirebaseLibraryAccessors getFirebase() { return laccForFirebaseLibraryAccessors; }

    /**
     * Returns the group of libraries at glide
     */
    public GlideLibraryAccessors getGlide() { return laccForGlideLibraryAccessors; }

    /**
     * Returns the group of libraries at hilt
     */
    public HiltLibraryAccessors getHilt() { return laccForHiltLibraryAccessors; }

    /**
     * Returns the group of libraries at javax
     */
    public JavaxLibraryAccessors getJavax() { return laccForJavaxLibraryAccessors; }

    /**
     * Returns the group of libraries at koin
     */
    public KoinLibraryAccessors getKoin() { return laccForKoinLibraryAccessors; }

    /**
     * Returns the group of libraries at kotlin
     */
    public KotlinLibraryAccessors getKotlin() { return laccForKotlinLibraryAccessors; }

    /**
     * Returns the group of libraries at krop
     */
    public KropLibraryAccessors getKrop() { return laccForKropLibraryAccessors; }

    /**
     * Returns the group of libraries at legacySupport
     */
    public LegacySupportLibraryAccessors getLegacySupport() { return laccForLegacySupportLibraryAccessors; }

    /**
     * Returns the group of libraries at lifecycle
     */
    public LifecycleLibraryAccessors getLifecycle() { return laccForLifecycleLibraryAccessors; }

    /**
     * Returns the group of libraries at navigation
     */
    public NavigationLibraryAccessors getNavigation() { return laccForNavigationLibraryAccessors; }

    /**
     * Returns the group of libraries at okHttp
     */
    public OkHttpLibraryAccessors getOkHttp() { return laccForOkHttpLibraryAccessors; }

    /**
     * Returns the group of libraries at paging
     */
    public PagingLibraryAccessors getPaging() { return laccForPagingLibraryAccessors; }

    /**
     * Returns the group of libraries at picasso
     */
    public PicassoLibraryAccessors getPicasso() { return laccForPicassoLibraryAccessors; }

    /**
     * Returns the group of libraries at retrofit
     */
    public RetrofitLibraryAccessors getRetrofit() { return laccForRetrofitLibraryAccessors; }

    /**
     * Returns the group of libraries at room
     */
    public RoomLibraryAccessors getRoom() { return laccForRoomLibraryAccessors; }

    /**
     * Returns the group of libraries at ui
     */
    public UiLibraryAccessors getUi() { return laccForUiLibraryAccessors; }

    /**
     * Returns the group of versions at versions
     */
    public VersionAccessors getVersions() { return vaccForVersionAccessors; }

    /**
     * Returns the group of bundles at bundles
     */
    public BundleAccessors getBundles() { return baccForBundleAccessors; }

    /**
     * Returns the group of plugins at plugins
     */
    public PluginAccessors getPlugins() { return paccForPluginAccessors; }

    public static class AlgoliaLibraryAccessors extends SubDependencyFactory {

        public AlgoliaLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

            /**
             * Creates a dependency provider for android (com.algolia:instantsearch-android)
             * This dependency was declared in catalog libs.versions.toml
             */
            public Provider<MinimalExternalModuleDependency> getAndroid() { return create("algolia.android"); }

            /**
             * Creates a dependency provider for paging (com.algolia:instantsearch-android-paging3)
             * This dependency was declared in catalog libs.versions.toml
             */
            public Provider<MinimalExternalModuleDependency> getPaging() { return create("algolia.paging"); }

    }

    public static class AndroidLibraryAccessors extends SubDependencyFactory {

        public AndroidLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

            /**
             * Creates a dependency provider for core (androidx.core:core-ktx)
             * This dependency was declared in catalog libs.versions.toml
             */
            public Provider<MinimalExternalModuleDependency> getCore() { return create("android.core"); }

    }

    public static class AvatarViewLibraryAccessors extends SubDependencyFactory {

        public AvatarViewLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

            /**
             * Creates a dependency provider for avatarView (io.getstream:avatarview-coil)
             * This dependency was declared in catalog libs.versions.toml
             */
            public Provider<MinimalExternalModuleDependency> getAvatarView() { return create("avatarView.avatarView"); }

    }

    public static class CoroutinesLibraryAccessors extends SubDependencyFactory {

        public CoroutinesLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

            /**
             * Creates a dependency provider for android (org.jetbrains.kotlinx:kotlinx-coroutines-android)
             * This dependency was declared in catalog libs.versions.toml
             */
            public Provider<MinimalExternalModuleDependency> getAndroid() { return create("coroutines.android"); }

            /**
             * Creates a dependency provider for core (org.jetbrains.kotlinx:kotlinx-coroutines-core)
             * This dependency was declared in catalog libs.versions.toml
             */
            public Provider<MinimalExternalModuleDependency> getCore() { return create("coroutines.core"); }

    }

    public static class DatastoreLibraryAccessors extends SubDependencyFactory {

        public DatastoreLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

            /**
             * Creates a dependency provider for preferences (androidx.datastore:datastore-preferences)
             * This dependency was declared in catalog libs.versions.toml
             */
            public Provider<MinimalExternalModuleDependency> getPreferences() { return create("datastore.preferences"); }

            /**
             * Creates a dependency provider for proto (androidx.datastore:datastore)
             * This dependency was declared in catalog libs.versions.toml
             */
            public Provider<MinimalExternalModuleDependency> getProto() { return create("datastore.proto"); }

    }

    public static class FirebaseLibraryAccessors extends SubDependencyFactory {

        public FirebaseLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

            /**
             * Creates a dependency provider for adMob (com.google.android.gms:play-services-ads)
             * This dependency was declared in catalog libs.versions.toml
             */
            public Provider<MinimalExternalModuleDependency> getAdMob() { return create("firebase.adMob"); }

            /**
             * Creates a dependency provider for analytics (com.google.firebase:firebase-analytics-ktx)
             * This dependency was declared in catalog libs.versions.toml
             */
            public Provider<MinimalExternalModuleDependency> getAnalytics() { return create("firebase.analytics"); }

            /**
             * Creates a dependency provider for auth (com.google.firebase:firebase-auth-ktx)
             * This dependency was declared in catalog libs.versions.toml
             */
            public Provider<MinimalExternalModuleDependency> getAuth() { return create("firebase.auth"); }

            /**
             * Creates a dependency provider for cloudMessaging (com.google.firebase:firebase-messaging-ktx)
             * This dependency was declared in catalog libs.versions.toml
             */
            public Provider<MinimalExternalModuleDependency> getCloudMessaging() { return create("firebase.cloudMessaging"); }

            /**
             * Creates a dependency provider for cloudStorage (com.google.firebase:firebase-storage-ktx)
             * This dependency was declared in catalog libs.versions.toml
             */
            public Provider<MinimalExternalModuleDependency> getCloudStorage() { return create("firebase.cloudStorage"); }

            /**
             * Creates a dependency provider for coroutines (org.jetbrains.kotlinx:kotlinx-coroutines-play-services)
             * This dependency was declared in catalog libs.versions.toml
             */
            public Provider<MinimalExternalModuleDependency> getCoroutines() { return create("firebase.coroutines"); }

            /**
             * Creates a dependency provider for crashlytics (com.google.firebase:firebase-crashlytics-ktx)
             * This dependency was declared in catalog libs.versions.toml
             */
            public Provider<MinimalExternalModuleDependency> getCrashlytics() { return create("firebase.crashlytics"); }

            /**
             * Creates a dependency provider for firestore (com.google.firebase:firebase-firestore-ktx)
             * This dependency was declared in catalog libs.versions.toml
             */
            public Provider<MinimalExternalModuleDependency> getFirestore() { return create("firebase.firestore"); }

            /**
             * Creates a dependency provider for inAppMessaging (com.google.firebase:firebase-inappmessaging-ktx)
             * This dependency was declared in catalog libs.versions.toml
             */
            public Provider<MinimalExternalModuleDependency> getInAppMessaging() { return create("firebase.inAppMessaging"); }

            /**
             * Creates a dependency provider for inAppMessagingDisplay (com.google.firebase:firebase-inappmessaging-display-ktx)
             * This dependency was declared in catalog libs.versions.toml
             */
            public Provider<MinimalExternalModuleDependency> getInAppMessagingDisplay() { return create("firebase.inAppMessagingDisplay"); }

            /**
             * Creates a dependency provider for platform (com.google.firebase:firebase-bom)
             * This dependency was declared in catalog libs.versions.toml
             */
            public Provider<MinimalExternalModuleDependency> getPlatform() { return create("firebase.platform"); }

            /**
             * Creates a dependency provider for uiStorage (com.firebaseui:firebase-ui-storage)
             * This dependency was declared in catalog libs.versions.toml
             */
            public Provider<MinimalExternalModuleDependency> getUiStorage() { return create("firebase.uiStorage"); }

    }

    public static class GlideLibraryAccessors extends SubDependencyFactory {

        public GlideLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

            /**
             * Creates a dependency provider for glide (com.github.bumptech.glide:glide)
             * This dependency was declared in catalog libs.versions.toml
             */
            public Provider<MinimalExternalModuleDependency> getGlide() { return create("glide.glide"); }

    }

    public static class HiltLibraryAccessors extends SubDependencyFactory {

        public HiltLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

            /**
             * Creates a dependency provider for android (com.google.dagger:hilt-android)
             * This dependency was declared in catalog libs.versions.toml
             */
            public Provider<MinimalExternalModuleDependency> getAndroid() { return create("hilt.android"); }

            /**
             * Creates a dependency provider for compiler (com.google.dagger:hilt-compiler)
             * This dependency was declared in catalog libs.versions.toml
             */
            public Provider<MinimalExternalModuleDependency> getCompiler() { return create("hilt.compiler"); }

            /**
             * Creates a dependency provider for viewModels (androidx.hilt:hilt-navigation-fragment)
             * This dependency was declared in catalog libs.versions.toml
             */
            public Provider<MinimalExternalModuleDependency> getViewModels() { return create("hilt.viewModels"); }

    }

    public static class JavaxLibraryAccessors extends SubDependencyFactory {

        public JavaxLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

            /**
             * Creates a dependency provider for inject (javax.inject:javax.inject)
             * This dependency was declared in catalog libs.versions.toml
             */
            public Provider<MinimalExternalModuleDependency> getInject() { return create("javax.inject"); }

    }

    public static class KoinLibraryAccessors extends SubDependencyFactory {

        public KoinLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

            /**
             * Creates a dependency provider for koin (io.insert-koin:koin-android)
             * This dependency was declared in catalog libs.versions.toml
             */
            public Provider<MinimalExternalModuleDependency> getKoin() { return create("koin.koin"); }

    }

    public static class KotlinLibraryAccessors extends SubDependencyFactory {

        public KotlinLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

            /**
             * Creates a dependency provider for gradle (org.jetbrains.kotlin:kotlin-gradle-plugin)
             * This dependency was declared in catalog libs.versions.toml
             */
            public Provider<MinimalExternalModuleDependency> getGradle() { return create("kotlin.gradle"); }

    }

    public static class KropLibraryAccessors extends SubDependencyFactory {

        public KropLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

            /**
             * Creates a dependency provider for krop (com.github.avito-tech:krop)
             * This dependency was declared in catalog libs.versions.toml
             */
            public Provider<MinimalExternalModuleDependency> getKrop() { return create("krop.krop"); }

    }

    public static class LegacySupportLibraryAccessors extends SubDependencyFactory {

        public LegacySupportLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

            /**
             * Creates a dependency provider for legacySupport (androidx.legacy:legacy-support-v4)
             * This dependency was declared in catalog libs.versions.toml
             */
            public Provider<MinimalExternalModuleDependency> getLegacySupport() { return create("legacySupport.legacySupport"); }

    }

    public static class LifecycleLibraryAccessors extends SubDependencyFactory {

        public LifecycleLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

            /**
             * Creates a dependency provider for liveData (androidx.lifecycle:lifecycle-livedata-ktx)
             * This dependency was declared in catalog libs.versions.toml
             */
            public Provider<MinimalExternalModuleDependency> getLiveData() { return create("lifecycle.liveData"); }

            /**
             * Creates a dependency provider for runtime (androidx.lifecycle:lifecycle-runtime-ktx)
             * This dependency was declared in catalog libs.versions.toml
             */
            public Provider<MinimalExternalModuleDependency> getRuntime() { return create("lifecycle.runtime"); }

            /**
             * Creates a dependency provider for viewModel (androidx.lifecycle:lifecycle-viewmodel-ktx)
             * This dependency was declared in catalog libs.versions.toml
             */
            public Provider<MinimalExternalModuleDependency> getViewModel() { return create("lifecycle.viewModel"); }

    }

    public static class NavigationLibraryAccessors extends SubDependencyFactory {

        public NavigationLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

            /**
             * Creates a dependency provider for fragment (androidx.navigation:navigation-fragment-ktx)
             * This dependency was declared in catalog libs.versions.toml
             */
            public Provider<MinimalExternalModuleDependency> getFragment() { return create("navigation.fragment"); }

            /**
             * Creates a dependency provider for ui (androidx.navigation:navigation-ui-ktx)
             * This dependency was declared in catalog libs.versions.toml
             */
            public Provider<MinimalExternalModuleDependency> getUi() { return create("navigation.ui"); }

    }

    public static class OkHttpLibraryAccessors extends SubDependencyFactory {

        public OkHttpLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

            /**
             * Creates a dependency provider for bom (com.squareup.okhttp3:okhttp-bom)
             * This dependency was declared in catalog libs.versions.toml
             */
            public Provider<MinimalExternalModuleDependency> getBom() { return create("okHttp.bom"); }

            /**
             * Creates a dependency provider for loggingInterceptor (com.squareup.okhttp3:logging-interceptor)
             * This dependency was declared in catalog libs.versions.toml
             */
            public Provider<MinimalExternalModuleDependency> getLoggingInterceptor() { return create("okHttp.loggingInterceptor"); }

            /**
             * Creates a dependency provider for okHttp (com.squareup.okhttp3:okhttp)
             * This dependency was declared in catalog libs.versions.toml
             */
            public Provider<MinimalExternalModuleDependency> getOkHttp() { return create("okHttp.okHttp"); }

    }

    public static class PagingLibraryAccessors extends SubDependencyFactory {

        public PagingLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

            /**
             * Creates a dependency provider for common (androidx.paging:paging-common)
             * This dependency was declared in catalog libs.versions.toml
             */
            public Provider<MinimalExternalModuleDependency> getCommon() { return create("paging.common"); }

            /**
             * Creates a dependency provider for paging (androidx.paging:paging-runtime-ktx)
             * This dependency was declared in catalog libs.versions.toml
             */
            public Provider<MinimalExternalModuleDependency> getPaging() { return create("paging.paging"); }

    }

    public static class PicassoLibraryAccessors extends SubDependencyFactory {

        public PicassoLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

            /**
             * Creates a dependency provider for picasso (com.squareup.picasso:picasso)
             * This dependency was declared in catalog libs.versions.toml
             */
            public Provider<MinimalExternalModuleDependency> getPicasso() { return create("picasso.picasso"); }

    }

    public static class RetrofitLibraryAccessors extends SubDependencyFactory {

        public RetrofitLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

            /**
             * Creates a dependency provider for gsonConverter (com.squareup.retrofit2:converter-gson)
             * This dependency was declared in catalog libs.versions.toml
             */
            public Provider<MinimalExternalModuleDependency> getGsonConverter() { return create("retrofit.gsonConverter"); }

            /**
             * Creates a dependency provider for retrofit (com.squareup.retrofit2:retrofit)
             * This dependency was declared in catalog libs.versions.toml
             */
            public Provider<MinimalExternalModuleDependency> getRetrofit() { return create("retrofit.retrofit"); }

    }

    public static class RoomLibraryAccessors extends SubDependencyFactory {

        public RoomLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

            /**
             * Creates a dependency provider for compiler (androidx.room:room-compiler)
             * This dependency was declared in catalog libs.versions.toml
             */
            public Provider<MinimalExternalModuleDependency> getCompiler() { return create("room.compiler"); }

            /**
             * Creates a dependency provider for kotlinExtensionsAndCoroutinesSupport (androidx.room:room-ktx)
             * This dependency was declared in catalog libs.versions.toml
             */
            public Provider<MinimalExternalModuleDependency> getKotlinExtensionsAndCoroutinesSupport() { return create("room.kotlinExtensionsAndCoroutinesSupport"); }

            /**
             * Creates a dependency provider for runtime (androidx.room:room-runtime)
             * This dependency was declared in catalog libs.versions.toml
             */
            public Provider<MinimalExternalModuleDependency> getRuntime() { return create("room.runtime"); }

    }

    public static class UiLibraryAccessors extends SubDependencyFactory {

        public UiLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

            /**
             * Creates a dependency provider for activity (androidx.activity:activity-ktx)
             * This dependency was declared in catalog libs.versions.toml
             */
            public Provider<MinimalExternalModuleDependency> getActivity() { return create("ui.activity"); }

            /**
             * Creates a dependency provider for appCompat (androidx.appcompat:appcompat)
             * This dependency was declared in catalog libs.versions.toml
             */
            public Provider<MinimalExternalModuleDependency> getAppCompat() { return create("ui.appCompat"); }

            /**
             * Creates a dependency provider for audioRecorderView (com.github.tougee:audiorecorderview)
             * This dependency was declared in catalog libs.versions.toml
             */
            public Provider<MinimalExternalModuleDependency> getAudioRecorderView() { return create("ui.audioRecorderView"); }

            /**
             * Creates a dependency provider for constraint (androidx.constraintlayout:constraintlayout)
             * This dependency was declared in catalog libs.versions.toml
             */
            public Provider<MinimalExternalModuleDependency> getConstraint() { return create("ui.constraint"); }

            /**
             * Creates a dependency provider for dotsIndicator (com.tbuonomo:dotsindicator)
             * This dependency was declared in catalog libs.versions.toml
             */
            public Provider<MinimalExternalModuleDependency> getDotsIndicator() { return create("ui.dotsIndicator"); }

            /**
             * Creates a dependency provider for emojiGoogle (com.vanniktech:emoji-google)
             * This dependency was declared in catalog libs.versions.toml
             */
            public Provider<MinimalExternalModuleDependency> getEmojiGoogle() { return create("ui.emojiGoogle"); }

            /**
             * Creates a dependency provider for emojiMaterial (com.vanniktech:emoji-material)
             * This dependency was declared in catalog libs.versions.toml
             */
            public Provider<MinimalExternalModuleDependency> getEmojiMaterial() { return create("ui.emojiMaterial"); }

            /**
             * Creates a dependency provider for fragment (androidx.fragment:fragment-ktx)
             * This dependency was declared in catalog libs.versions.toml
             */
            public Provider<MinimalExternalModuleDependency> getFragment() { return create("ui.fragment"); }

            /**
             * Creates a dependency provider for maskedEditText (io.github.vicmikhailau:MaskedEditText)
             * This dependency was declared in catalog libs.versions.toml
             */
            public Provider<MinimalExternalModuleDependency> getMaskedEditText() { return create("ui.maskedEditText"); }

            /**
             * Creates a dependency provider for material (com.google.android.material:material)
             * This dependency was declared in catalog libs.versions.toml
             */
            public Provider<MinimalExternalModuleDependency> getMaterial() { return create("ui.material"); }

            /**
             * Creates a dependency provider for splashScreen (androidx.core:core-splashscreen)
             * This dependency was declared in catalog libs.versions.toml
             */
            public Provider<MinimalExternalModuleDependency> getSplashScreen() { return create("ui.splashScreen"); }

            /**
             * Creates a dependency provider for viewBindingPropertyDelegate (com.github.kirich1409:viewbindingpropertydelegate-noreflection)
             * This dependency was declared in catalog libs.versions.toml
             */
            public Provider<MinimalExternalModuleDependency> getViewBindingPropertyDelegate() { return create("ui.viewBindingPropertyDelegate"); }

            /**
             * Creates a dependency provider for waveformSeekBar (com.github.massoudss:waveformSeekBar)
             * This dependency was declared in catalog libs.versions.toml
             */
            public Provider<MinimalExternalModuleDependency> getWaveformSeekBar() { return create("ui.waveformSeekBar"); }

            /**
             * Creates a dependency provider for waveformSeekBarAmplituda (com.github.massoudss:waveformSeekBar)
             * This dependency was declared in catalog libs.versions.toml
             */
            public Provider<MinimalExternalModuleDependency> getWaveformSeekBarAmplituda() { return create("ui.waveformSeekBarAmplituda"); }

    }

    public static class VersionAccessors extends VersionFactory  {

        public VersionAccessors(ProviderFactory providers, DefaultVersionCatalog config) { super(providers, config); }

            /**
             * Returns the version associated to this alias: activity (1.5.1)
             * If the version is a rich version and that its not expressible as a
             * single version string, then an empty string is returned.
             * This version was declared in catalog libs.versions.toml
             */
            public Provider<String> getActivity() { return getVersion("activity"); }

            /**
             * Returns the version associated to this alias: agp (7.2.2)
             * If the version is a rich version and that its not expressible as a
             * single version string, then an empty string is returned.
             * This version was declared in catalog libs.versions.toml
             */
            public Provider<String> getAgp() { return getVersion("agp"); }

            /**
             * Returns the version associated to this alias: algolia (3.1.3)
             * If the version is a rich version and that its not expressible as a
             * single version string, then an empty string is returned.
             * This version was declared in catalog libs.versions.toml
             */
            public Provider<String> getAlgolia() { return getVersion("algolia"); }

            /**
             * Returns the version associated to this alias: appCompat (1.5.1)
             * If the version is a rich version and that its not expressible as a
             * single version string, then an empty string is returned.
             * This version was declared in catalog libs.versions.toml
             */
            public Provider<String> getAppCompat() { return getVersion("appCompat"); }

            /**
             * Returns the version associated to this alias: audioRecorderView (1.0.4)
             * If the version is a rich version and that its not expressible as a
             * single version string, then an empty string is returned.
             * This version was declared in catalog libs.versions.toml
             */
            public Provider<String> getAudioRecorderView() { return getVersion("audioRecorderView"); }

            /**
             * Returns the version associated to this alias: avatarView (1.0.6)
             * If the version is a rich version and that its not expressible as a
             * single version string, then an empty string is returned.
             * This version was declared in catalog libs.versions.toml
             */
            public Provider<String> getAvatarView() { return getVersion("avatarView"); }

            /**
             * Returns the version associated to this alias: compileSdk (32)
             * If the version is a rich version and that its not expressible as a
             * single version string, then an empty string is returned.
             * This version was declared in catalog libs.versions.toml
             */
            public Provider<String> getCompileSdk() { return getVersion("compileSdk"); }

            /**
             * Returns the version associated to this alias: constraint (2.1.4)
             * If the version is a rich version and that its not expressible as a
             * single version string, then an empty string is returned.
             * This version was declared in catalog libs.versions.toml
             */
            public Provider<String> getConstraint() { return getVersion("constraint"); }

            /**
             * Returns the version associated to this alias: core (1.8.0)
             * If the version is a rich version and that its not expressible as a
             * single version string, then an empty string is returned.
             * This version was declared in catalog libs.versions.toml
             */
            public Provider<String> getCore() { return getVersion("core"); }

            /**
             * Returns the version associated to this alias: coroutines (1.6.4)
             * If the version is a rich version and that its not expressible as a
             * single version string, then an empty string is returned.
             * This version was declared in catalog libs.versions.toml
             */
            public Provider<String> getCoroutines() { return getVersion("coroutines"); }

            /**
             * Returns the version associated to this alias: datastore (1.0.0)
             * If the version is a rich version and that its not expressible as a
             * single version string, then an empty string is returned.
             * This version was declared in catalog libs.versions.toml
             */
            public Provider<String> getDatastore() { return getVersion("datastore"); }

            /**
             * Returns the version associated to this alias: dotsIndicator (4.3)
             * If the version is a rich version and that its not expressible as a
             * single version string, then an empty string is returned.
             * This version was declared in catalog libs.versions.toml
             */
            public Provider<String> getDotsIndicator() { return getVersion("dotsIndicator"); }

            /**
             * Returns the version associated to this alias: emojiPopUp (0.15.0)
             * If the version is a rich version and that its not expressible as a
             * single version string, then an empty string is returned.
             * This version was declared in catalog libs.versions.toml
             */
            public Provider<String> getEmojiPopUp() { return getVersion("emojiPopUp"); }

            /**
             * Returns the version associated to this alias: firebaseAdMob (21.0.0)
             * If the version is a rich version and that its not expressible as a
             * single version string, then an empty string is returned.
             * This version was declared in catalog libs.versions.toml
             */
            public Provider<String> getFirebaseAdMob() { return getVersion("firebaseAdMob"); }

            /**
             * Returns the version associated to this alias: firebaseAnalytics (21.0.0)
             * If the version is a rich version and that its not expressible as a
             * single version string, then an empty string is returned.
             * This version was declared in catalog libs.versions.toml
             */
            public Provider<String> getFirebaseAnalytics() { return getVersion("firebaseAnalytics"); }

            /**
             * Returns the version associated to this alias: firebaseCloudMessaging (23.0.5)
             * If the version is a rich version and that its not expressible as a
             * single version string, then an empty string is returned.
             * This version was declared in catalog libs.versions.toml
             */
            public Provider<String> getFirebaseCloudMessaging() { return getVersion("firebaseCloudMessaging"); }

            /**
             * Returns the version associated to this alias: firebaseCloudStorage (20.0.1)
             * If the version is a rich version and that its not expressible as a
             * single version string, then an empty string is returned.
             * This version was declared in catalog libs.versions.toml
             */
            public Provider<String> getFirebaseCloudStorage() { return getVersion("firebaseCloudStorage"); }

            /**
             * Returns the version associated to this alias: firebaseCoroutines (1.6.1)
             * If the version is a rich version and that its not expressible as a
             * single version string, then an empty string is returned.
             * This version was declared in catalog libs.versions.toml
             */
            public Provider<String> getFirebaseCoroutines() { return getVersion("firebaseCoroutines"); }

            /**
             * Returns the version associated to this alias: firebaseCrashlytics (2.8.1)
             * If the version is a rich version and that its not expressible as a
             * single version string, then an empty string is returned.
             * This version was declared in catalog libs.versions.toml
             */
            public Provider<String> getFirebaseCrashlytics() { return getVersion("firebaseCrashlytics"); }

            /**
             * Returns the version associated to this alias: firebaseFirestore (24.1.2)
             * If the version is a rich version and that its not expressible as a
             * single version string, then an empty string is returned.
             * This version was declared in catalog libs.versions.toml
             */
            public Provider<String> getFirebaseFirestore() { return getVersion("firebaseFirestore"); }

            /**
             * Returns the version associated to this alias: firebaseInAppMessaging (20.1.2)
             * If the version is a rich version and that its not expressible as a
             * single version string, then an empty string is returned.
             * This version was declared in catalog libs.versions.toml
             */
            public Provider<String> getFirebaseInAppMessaging() { return getVersion("firebaseInAppMessaging"); }

            /**
             * Returns the version associated to this alias: firebasePlatform (30.1.0)
             * If the version is a rich version and that its not expressible as a
             * single version string, then an empty string is returned.
             * This version was declared in catalog libs.versions.toml
             */
            public Provider<String> getFirebasePlatform() { return getVersion("firebasePlatform"); }

            /**
             * Returns the version associated to this alias: firebaseUiStorage (8.0.1)
             * If the version is a rich version and that its not expressible as a
             * single version string, then an empty string is returned.
             * This version was declared in catalog libs.versions.toml
             */
            public Provider<String> getFirebaseUiStorage() { return getVersion("firebaseUiStorage"); }

            /**
             * Returns the version associated to this alias: fragment (1.5.2)
             * If the version is a rich version and that its not expressible as a
             * single version string, then an empty string is returned.
             * This version was declared in catalog libs.versions.toml
             */
            public Provider<String> getFragment() { return getVersion("fragment"); }

            /**
             * Returns the version associated to this alias: glide (4.13.0)
             * If the version is a rich version and that its not expressible as a
             * single version string, then an empty string is returned.
             * This version was declared in catalog libs.versions.toml
             */
            public Provider<String> getGlide() { return getVersion("glide"); }

            /**
             * Returns the version associated to this alias: googleServices (4.3.10)
             * If the version is a rich version and that its not expressible as a
             * single version string, then an empty string is returned.
             * This version was declared in catalog libs.versions.toml
             */
            public Provider<String> getGoogleServices() { return getVersion("googleServices"); }

            /**
             * Returns the version associated to this alias: gradle (7.5.0)
             * If the version is a rich version and that its not expressible as a
             * single version string, then an empty string is returned.
             * This version was declared in catalog libs.versions.toml
             */
            public Provider<String> getGradle() { return getVersion("gradle"); }

            /**
             * Returns the version associated to this alias: hilt (2.43.2)
             * If the version is a rich version and that its not expressible as a
             * single version string, then an empty string is returned.
             * This version was declared in catalog libs.versions.toml
             */
            public Provider<String> getHilt() { return getVersion("hilt"); }

            /**
             * Returns the version associated to this alias: hiltNavGraphViewModels (1.0.0)
             * If the version is a rich version and that its not expressible as a
             * single version string, then an empty string is returned.
             * This version was declared in catalog libs.versions.toml
             */
            public Provider<String> getHiltNavGraphViewModels() { return getVersion("hiltNavGraphViewModels"); }

            /**
             * Returns the version associated to this alias: javax (1)
             * If the version is a rich version and that its not expressible as a
             * single version string, then an empty string is returned.
             * This version was declared in catalog libs.versions.toml
             */
            public Provider<String> getJavax() { return getVersion("javax"); }

            /**
             * Returns the version associated to this alias: koin (3.2.0-beta-1)
             * If the version is a rich version and that its not expressible as a
             * single version string, then an empty string is returned.
             * This version was declared in catalog libs.versions.toml
             */
            public Provider<String> getKoin() { return getVersion("koin"); }

            /**
             * Returns the version associated to this alias: kotlin (1.7.10)
             * If the version is a rich version and that its not expressible as a
             * single version string, then an empty string is returned.
             * This version was declared in catalog libs.versions.toml
             */
            public Provider<String> getKotlin() { return getVersion("kotlin"); }

            /**
             * Returns the version associated to this alias: krop (0.64)
             * If the version is a rich version and that its not expressible as a
             * single version string, then an empty string is returned.
             * This version was declared in catalog libs.versions.toml
             */
            public Provider<String> getKrop() { return getVersion("krop"); }

            /**
             * Returns the version associated to this alias: legacySupport (1.0.0)
             * If the version is a rich version and that its not expressible as a
             * single version string, then an empty string is returned.
             * This version was declared in catalog libs.versions.toml
             */
            public Provider<String> getLegacySupport() { return getVersion("legacySupport"); }

            /**
             * Returns the version associated to this alias: lifecycle (2.5.1)
             * If the version is a rich version and that its not expressible as a
             * single version string, then an empty string is returned.
             * This version was declared in catalog libs.versions.toml
             */
            public Provider<String> getLifecycle() { return getVersion("lifecycle"); }

            /**
             * Returns the version associated to this alias: maskedEditText (4.0.7)
             * If the version is a rich version and that its not expressible as a
             * single version string, then an empty string is returned.
             * This version was declared in catalog libs.versions.toml
             */
            public Provider<String> getMaskedEditText() { return getVersion("maskedEditText"); }

            /**
             * Returns the version associated to this alias: material (1.6.1)
             * If the version is a rich version and that its not expressible as a
             * single version string, then an empty string is returned.
             * This version was declared in catalog libs.versions.toml
             */
            public Provider<String> getMaterial() { return getVersion("material"); }

            /**
             * Returns the version associated to this alias: minSdk (23)
             * If the version is a rich version and that its not expressible as a
             * single version string, then an empty string is returned.
             * This version was declared in catalog libs.versions.toml
             */
            public Provider<String> getMinSdk() { return getVersion("minSdk"); }

            /**
             * Returns the version associated to this alias: navigation (2.5.2)
             * If the version is a rich version and that its not expressible as a
             * single version string, then an empty string is returned.
             * This version was declared in catalog libs.versions.toml
             */
            public Provider<String> getNavigation() { return getVersion("navigation"); }

            /**
             * Returns the version associated to this alias: okHttp (5.0.0-alpha.10)
             * If the version is a rich version and that its not expressible as a
             * single version string, then an empty string is returned.
             * This version was declared in catalog libs.versions.toml
             */
            public Provider<String> getOkHttp() { return getVersion("okHttp"); }

            /**
             * Returns the version associated to this alias: paging (3.1.1)
             * If the version is a rich version and that its not expressible as a
             * single version string, then an empty string is returned.
             * This version was declared in catalog libs.versions.toml
             */
            public Provider<String> getPaging() { return getVersion("paging"); }

            /**
             * Returns the version associated to this alias: picasso (2.71828)
             * If the version is a rich version and that its not expressible as a
             * single version string, then an empty string is returned.
             * This version was declared in catalog libs.versions.toml
             */
            public Provider<String> getPicasso() { return getVersion("picasso"); }

            /**
             * Returns the version associated to this alias: retrofit (2.9.0)
             * If the version is a rich version and that its not expressible as a
             * single version string, then an empty string is returned.
             * This version was declared in catalog libs.versions.toml
             */
            public Provider<String> getRetrofit() { return getVersion("retrofit"); }

            /**
             * Returns the version associated to this alias: room (2.4.3)
             * If the version is a rich version and that its not expressible as a
             * single version string, then an empty string is returned.
             * This version was declared in catalog libs.versions.toml
             */
            public Provider<String> getRoom() { return getVersion("room"); }

            /**
             * Returns the version associated to this alias: splashScreen (1.0.0-beta01)
             * If the version is a rich version and that its not expressible as a
             * single version string, then an empty string is returned.
             * This version was declared in catalog libs.versions.toml
             */
            public Provider<String> getSplashScreen() { return getVersion("splashScreen"); }

            /**
             * Returns the version associated to this alias: targetSdk (32)
             * If the version is a rich version and that its not expressible as a
             * single version string, then an empty string is returned.
             * This version was declared in catalog libs.versions.toml
             */
            public Provider<String> getTargetSdk() { return getVersion("targetSdk"); }

            /**
             * Returns the version associated to this alias: viewBindingPropertyDelegate (1.5.6)
             * If the version is a rich version and that its not expressible as a
             * single version string, then an empty string is returned.
             * This version was declared in catalog libs.versions.toml
             */
            public Provider<String> getViewBindingPropertyDelegate() { return getVersion("viewBindingPropertyDelegate"); }

            /**
             * Returns the version associated to this alias: waveformAmplituda (2.1.4)
             * If the version is a rich version and that its not expressible as a
             * single version string, then an empty string is returned.
             * This version was declared in catalog libs.versions.toml
             */
            public Provider<String> getWaveformAmplituda() { return getVersion("waveformAmplituda"); }

            /**
             * Returns the version associated to this alias: waveformSeekBar (5.0.1)
             * If the version is a rich version and that its not expressible as a
             * single version string, then an empty string is returned.
             * This version was declared in catalog libs.versions.toml
             */
            public Provider<String> getWaveformSeekBar() { return getVersion("waveformSeekBar"); }

    }

    public static class BundleAccessors extends BundleFactory {

        public BundleAccessors(ProviderFactory providers, DefaultVersionCatalog config) { super(providers, config); }

            /**
             * Creates a dependency bundle provider for algolia which is an aggregate for the following dependencies:
             * <ul>
             *    <li>com.algolia:instantsearch-android</li>
             *    <li>com.algolia:instantsearch-android-paging3</li>
             * </ul>
             * This bundle was declared in catalog libs.versions.toml
             */
            public Provider<ExternalModuleDependencyBundle> getAlgolia() { return createBundle("algolia"); }

            /**
             * Creates a dependency bundle provider for coroutines which is an aggregate for the following dependencies:
             * <ul>
             *    <li>org.jetbrains.kotlinx:kotlinx-coroutines-core</li>
             *    <li>org.jetbrains.kotlinx:kotlinx-coroutines-android</li>
             * </ul>
             * This bundle was declared in catalog libs.versions.toml
             */
            public Provider<ExternalModuleDependencyBundle> getCoroutines() { return createBundle("coroutines"); }

            /**
             * Creates a dependency bundle provider for datastore which is an aggregate for the following dependencies:
             * <ul>
             *    <li>androidx.datastore:datastore-preferences</li>
             *    <li>androidx.datastore:datastore</li>
             * </ul>
             * This bundle was declared in catalog libs.versions.toml
             */
            public Provider<ExternalModuleDependencyBundle> getDatastore() { return createBundle("datastore"); }

            /**
             * Creates a dependency bundle provider for firebase which is an aggregate for the following dependencies:
             * <ul>
             *    <li>com.google.firebase:firebase-firestore-ktx</li>
             *    <li>com.google.firebase:firebase-bom</li>
             *    <li>org.jetbrains.kotlinx:kotlinx-coroutines-play-services</li>
             * </ul>
             * This bundle was declared in catalog libs.versions.toml
             */
            public Provider<ExternalModuleDependencyBundle> getFirebase() { return createBundle("firebase"); }

            /**
             * Creates a dependency bundle provider for firebaseNoAdMobAndCrashlytics which is an aggregate for the following dependencies:
             * <ul>
             *    <li>com.google.firebase:firebase-firestore-ktx</li>
             *    <li>com.google.firebase:firebase-storage-ktx</li>
             *    <li>org.jetbrains.kotlinx:kotlinx-coroutines-play-services</li>
             *    <li>com.google.firebase:firebase-analytics-ktx</li>
             *    <li>com.google.firebase:firebase-auth-ktx</li>
             *    <li>com.google.firebase:firebase-messaging-ktx</li>
             *    <li>com.google.firebase:firebase-inappmessaging-ktx</li>
             *    <li>com.google.firebase:firebase-inappmessaging-display-ktx</li>
             *    <li>com.firebaseui:firebase-ui-storage</li>
             * </ul>
             * This bundle was declared in catalog libs.versions.toml
             */
            public Provider<ExternalModuleDependencyBundle> getFirebaseNoAdMobAndCrashlytics() { return createBundle("firebaseNoAdMobAndCrashlytics"); }

            /**
             * Creates a dependency bundle provider for firebaseNoCrashlytics which is an aggregate for the following dependencies:
             * <ul>
             *    <li>com.google.firebase:firebase-firestore-ktx</li>
             *    <li>com.google.firebase:firebase-storage-ktx</li>
             *    <li>org.jetbrains.kotlinx:kotlinx-coroutines-play-services</li>
             *    <li>com.google.firebase:firebase-analytics-ktx</li>
             *    <li>com.google.firebase:firebase-auth-ktx</li>
             *    <li>com.google.firebase:firebase-messaging-ktx</li>
             *    <li>com.google.firebase:firebase-inappmessaging-ktx</li>
             *    <li>com.google.firebase:firebase-inappmessaging-display-ktx</li>
             *    <li>com.google.android.gms:play-services-ads</li>
             * </ul>
             * This bundle was declared in catalog libs.versions.toml
             */
            public Provider<ExternalModuleDependencyBundle> getFirebaseNoCrashlytics() { return createBundle("firebaseNoCrashlytics"); }

            /**
             * Creates a dependency bundle provider for hilt which is an aggregate for the following dependencies:
             * <ul>
             *    <li>com.google.dagger:hilt-android</li>
             *    <li>androidx.hilt:hilt-navigation-fragment</li>
             * </ul>
             * This bundle was declared in catalog libs.versions.toml
             */
            public Provider<ExternalModuleDependencyBundle> getHilt() { return createBundle("hilt"); }

            /**
             * Creates a dependency bundle provider for lifecycle which is an aggregate for the following dependencies:
             * <ul>
             *    <li>androidx.lifecycle:lifecycle-viewmodel-ktx</li>
             *    <li>androidx.lifecycle:lifecycle-runtime-ktx</li>
             *    <li>androidx.lifecycle:lifecycle-livedata-ktx</li>
             * </ul>
             * This bundle was declared in catalog libs.versions.toml
             */
            public Provider<ExternalModuleDependencyBundle> getLifecycle() { return createBundle("lifecycle"); }

            /**
             * Creates a dependency bundle provider for navigation which is an aggregate for the following dependencies:
             * <ul>
             *    <li>androidx.navigation:navigation-fragment-ktx</li>
             *    <li>androidx.navigation:navigation-ui-ktx</li>
             * </ul>
             * This bundle was declared in catalog libs.versions.toml
             */
            public Provider<ExternalModuleDependencyBundle> getNavigation() { return createBundle("navigation"); }

            /**
             * Creates a dependency bundle provider for okHttp which is an aggregate for the following dependencies:
             * <ul>
             *    <li>com.squareup.okhttp3:okhttp-bom</li>
             *    <li>com.squareup.okhttp3:logging-interceptor</li>
             *    <li>com.squareup.okhttp3:okhttp</li>
             * </ul>
             * This bundle was declared in catalog libs.versions.toml
             */
            public Provider<ExternalModuleDependencyBundle> getOkHttp() { return createBundle("okHttp"); }

            /**
             * Creates a dependency bundle provider for paging which is an aggregate for the following dependencies:
             * <ul>
             *    <li>androidx.paging:paging-runtime-ktx</li>
             *    <li>androidx.paging:paging-common</li>
             * </ul>
             * This bundle was declared in catalog libs.versions.toml
             */
            public Provider<ExternalModuleDependencyBundle> getPaging() { return createBundle("paging"); }

            /**
             * Creates a dependency bundle provider for retrofit which is an aggregate for the following dependencies:
             * <ul>
             *    <li>com.squareup.retrofit2:retrofit</li>
             *    <li>com.squareup.retrofit2:converter-gson</li>
             * </ul>
             * This bundle was declared in catalog libs.versions.toml
             */
            public Provider<ExternalModuleDependencyBundle> getRetrofit() { return createBundle("retrofit"); }

            /**
             * Creates a dependency bundle provider for room which is an aggregate for the following dependencies:
             * <ul>
             *    <li>androidx.room:room-runtime</li>
             *    <li>androidx.room:room-ktx</li>
             * </ul>
             * This bundle was declared in catalog libs.versions.toml
             */
            public Provider<ExternalModuleDependencyBundle> getRoom() { return createBundle("room"); }

            /**
             * Creates a dependency bundle provider for uiComponents which is an aggregate for the following dependencies:
             * <ul>
             *    <li>androidx.activity:activity-ktx</li>
             *    <li>androidx.fragment:fragment-ktx</li>
             *    <li>androidx.appcompat:appcompat</li>
             *    <li>androidx.constraintlayout:constraintlayout</li>
             *    <li>com.google.android.material:material</li>
             *    <li>com.github.kirich1409:viewbindingpropertydelegate-noreflection</li>
             *    <li>com.github.bumptech.glide:glide</li>
             *    <li>androidx.core:core-splashscreen</li>
             *    <li>io.github.vicmikhailau:MaskedEditText</li>
             *    <li>com.tbuonomo:dotsindicator</li>
             *    <li>com.vanniktech:emoji-material</li>
             *    <li>com.vanniktech:emoji-google</li>
             *    <li>com.github.massoudss:waveformSeekBar</li>
             *    <li>com.github.massoudss:waveformSeekBar</li>
             *    <li>com.github.avito-tech:krop</li>
             *    <li>com.squareup.picasso:picasso</li>
             *    <li>io.getstream:avatarview-coil</li>
             * </ul>
             * This bundle was declared in catalog libs.versions.toml
             */
            public Provider<ExternalModuleDependencyBundle> getUiComponents() { return createBundle("uiComponents"); }

    }

    public static class PluginAccessors extends PluginFactory {
        private final AgpPluginAccessors baccForAgpPluginAccessors = new AgpPluginAccessors(providers, config);
        private final FirebasePluginAccessors baccForFirebasePluginAccessors = new FirebasePluginAccessors(providers, config);
        private final GooglePluginAccessors baccForGooglePluginAccessors = new GooglePluginAccessors(providers, config);
        private final HiltPluginAccessors baccForHiltPluginAccessors = new HiltPluginAccessors(providers, config);
        private final KotlinPluginAccessors baccForKotlinPluginAccessors = new KotlinPluginAccessors(providers, config);
        private final NavigationPluginAccessors baccForNavigationPluginAccessors = new NavigationPluginAccessors(providers, config);

        public PluginAccessors(ProviderFactory providers, DefaultVersionCatalog config) { super(providers, config); }

        /**
         * Returns the group of bundles at plugins.agp
         */
        public AgpPluginAccessors getAgp() { return baccForAgpPluginAccessors; }

        /**
         * Returns the group of bundles at plugins.firebase
         */
        public FirebasePluginAccessors getFirebase() { return baccForFirebasePluginAccessors; }

        /**
         * Returns the group of bundles at plugins.google
         */
        public GooglePluginAccessors getGoogle() { return baccForGooglePluginAccessors; }

        /**
         * Returns the group of bundles at plugins.hilt
         */
        public HiltPluginAccessors getHilt() { return baccForHiltPluginAccessors; }

        /**
         * Returns the group of bundles at plugins.kotlin
         */
        public KotlinPluginAccessors getKotlin() { return baccForKotlinPluginAccessors; }

        /**
         * Returns the group of bundles at plugins.navigation
         */
        public NavigationPluginAccessors getNavigation() { return baccForNavigationPluginAccessors; }

    }

    public static class AgpPluginAccessors extends PluginFactory {

        public AgpPluginAccessors(ProviderFactory providers, DefaultVersionCatalog config) { super(providers, config); }

            /**
             * Creates a plugin provider for agp.application to the plugin id 'com.android.application'
             * This plugin was declared in catalog libs.versions.toml
             */
            public Provider<PluginDependency> getApplication() { return createPlugin("agp.application"); }

            /**
             * Creates a plugin provider for agp.library to the plugin id 'com.android.library'
             * This plugin was declared in catalog libs.versions.toml
             */
            public Provider<PluginDependency> getLibrary() { return createPlugin("agp.library"); }

    }

    public static class FirebasePluginAccessors extends PluginFactory {

        public FirebasePluginAccessors(ProviderFactory providers, DefaultVersionCatalog config) { super(providers, config); }

            /**
             * Creates a plugin provider for firebase.crashlytics to the plugin id 'com.google.firebase.crashlytics'
             * This plugin was declared in catalog libs.versions.toml
             */
            public Provider<PluginDependency> getCrashlytics() { return createPlugin("firebase.crashlytics"); }

    }

    public static class GooglePluginAccessors extends PluginFactory {

        public GooglePluginAccessors(ProviderFactory providers, DefaultVersionCatalog config) { super(providers, config); }

            /**
             * Creates a plugin provider for google.services to the plugin id 'com.google.gms.google-services'
             * This plugin was declared in catalog libs.versions.toml
             */
            public Provider<PluginDependency> getServices() { return createPlugin("google.services"); }

    }

    public static class HiltPluginAccessors extends PluginFactory {

        public HiltPluginAccessors(ProviderFactory providers, DefaultVersionCatalog config) { super(providers, config); }

            /**
             * Creates a plugin provider for hilt.android to the plugin id 'com.google.dagger.hilt.android'
             * This plugin was declared in catalog libs.versions.toml
             */
            public Provider<PluginDependency> getAndroid() { return createPlugin("hilt.android"); }

    }

    public static class KotlinPluginAccessors extends PluginFactory {

        public KotlinPluginAccessors(ProviderFactory providers, DefaultVersionCatalog config) { super(providers, config); }

            /**
             * Creates a plugin provider for kotlin.gradle to the plugin id 'org.jetbrains.kotlin.android'
             * This plugin was declared in catalog libs.versions.toml
             */
            public Provider<PluginDependency> getGradle() { return createPlugin("kotlin.gradle"); }

            /**
             * Creates a plugin provider for kotlin.serialization to the plugin id 'org.jetbrains.kotlin.plugin.serialization'
             * This plugin was declared in catalog libs.versions.toml
             */
            public Provider<PluginDependency> getSerialization() { return createPlugin("kotlin.serialization"); }

    }

    public static class NavigationPluginAccessors extends PluginFactory {

        public NavigationPluginAccessors(ProviderFactory providers, DefaultVersionCatalog config) { super(providers, config); }

            /**
             * Creates a plugin provider for navigation.safeArgs to the plugin id 'androidx.navigation.safeargs.kotlin'
             * This plugin was declared in catalog libs.versions.toml
             */
            public Provider<PluginDependency> getSafeArgs() { return createPlugin("navigation.safeArgs"); }

    }

}
