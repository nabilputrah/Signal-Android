package org.thoughtcrime.securesms.wallpaper;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavGraph;
import androidx.navigation.Navigation;

import org.thoughtcrime.securesms.PassphraseRequiredActivity;
import org.thoughtcrime.securesms.R;
import org.thoughtcrime.securesms.recipients.RecipientId;
import org.thoughtcrime.securesms.util.ActivityTransitionUtil;
import org.thoughtcrime.securesms.util.DynamicNoActionBarTheme;
import org.thoughtcrime.securesms.util.DynamicTheme;

public final class HomeWallpaperActivity extends PassphraseRequiredActivity {

    private static final String EXTRA_RECIPIENT_ID = "extra.recipient.id";

    private final DynamicTheme dynamicTheme = new DynamicNoActionBarTheme();

    public static @NonNull Intent createIntent(@NonNull Context context) {
        return createIntent(context, null);
    }

    public static @NonNull Intent createIntent(@NonNull Context context, @Nullable RecipientId recipientId) {
        Intent intent = new Intent(context, HomeWallpaperActivity.class);
        intent.putExtra(EXTRA_RECIPIENT_ID, recipientId);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState, boolean ready) {
        HomeWallpaperViewModel.Factory factory = new HomeWallpaperViewModel.Factory(getIntent().getParcelableExtra(EXTRA_RECIPIENT_ID));
        ViewModelProviders.of(this, factory).get(HomeWallpaperViewModel.class);

        dynamicTheme.onCreate(this);
        setContentView(R.layout.home_wallpaper_activity);

        Toolbar toolbar = findViewById(R.id.toolbar);

        toolbar.setNavigationOnClickListener(unused -> {
            if (!Navigation.findNavController(this, R.id.nav_host_fragment).popBackStack()) {
                finish();
                ActivityTransitionUtil.setSlideOutTransition(this);
            }
        });

        if (savedInstanceState == null) {
            Bundle   extras = getIntent().getExtras();
            NavGraph graph  = Navigation.findNavController(this, R.id.nav_host_fragment).getGraph();

            Navigation.findNavController(this, R.id.nav_host_fragment).setGraph(graph, extras != null ? extras : new Bundle());
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        ActivityTransitionUtil.setSlideOutTransition(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        dynamicTheme.onResume(this);
    }
}
