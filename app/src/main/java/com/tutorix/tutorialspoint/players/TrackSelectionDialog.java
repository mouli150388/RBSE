package com.tutorix.tutorialspoint.players;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckedTextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialog;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.google.android.exoplayer2.C;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector.SelectionOverride;
import com.google.android.exoplayer2.trackselection.MappingTrackSelector.MappedTrackInfo;
import com.google.android.exoplayer2.ui.TrackSelectionView;
import com.google.android.exoplayer2.util.Assertions;
import com.google.android.material.tabs.TabLayout;
import com.tutorix.tutorialspoint.AppController;
import com.tutorix.tutorialspoint.R;
import com.tutorix.tutorialspoint.SessionManager;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TrackSelectionDialog extends DialogFragment {

    private final SparseArray<TrackSelectionViewFragment> tabFragments;
    private final ArrayList<Integer> tabTrackTypes;

    private int titleId;
    private DialogInterface.OnClickListener onClickListener;
    private DialogInterface.OnDismissListener onDismissListener;
     static int childQuality=0;
     static int childQualityAudio=-1;
     static int childQualityText=-1;
    /**
     * Returns whether a track selection dialog will have content to display if initialized with the
     * specified {@link DefaultTrackSelector} in its current state.
     */
    public static boolean willHaveContent(DefaultTrackSelector trackSelector) {
        MappedTrackInfo mappedTrackInfo = trackSelector.getCurrentMappedTrackInfo();
        return mappedTrackInfo != null && willHaveContent(mappedTrackInfo);
    }

    /**
     * Returns whether a track selection dialog will have content to display if initialized with the
     * specified {@link MappedTrackInfo}.
     */
    public static boolean willHaveContent(MappedTrackInfo mappedTrackInfo) {
        for (int i = 0; i < mappedTrackInfo.getRendererCount(); i++) {
            if (showTabForRenderer(mappedTrackInfo, i)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Creates a dialog for a given {@link DefaultTrackSelector}, whose parameters will be
     * automatically updated when tracks are selected.
     *
     * @param trackSelector The {@link DefaultTrackSelector}.
     * @param onDismissListener A {@link DialogInterface.OnDismissListener} to call when the dialog is
     *     dismissed.
     */
    public static TrackSelectionDialog createForTrackSelector(
            DefaultTrackSelector trackSelector, DialogInterface.OnDismissListener onDismissListener) {

      childQuality=AppController.childQaulity;
       childQualityAudio=AppController.childQaulityAudio;
        childQualityText=AppController.childQaulityText;


        MappedTrackInfo mappedTrackInfo =
                Assertions.checkNotNull(trackSelector.getCurrentMappedTrackInfo());
        TrackSelectionDialog trackSelectionDialog = new TrackSelectionDialog();
        DefaultTrackSelector.Parameters parameters = trackSelector.getParameters();
        trackSelectionDialog.init(
                /* titleId= */ R.string.track_selection_title,
                mappedTrackInfo,
                /* initialParameters = */ parameters,
                /* allowAdaptiveSelections =*/ true,
                /* allowMultipleOverrides= */ false,
                /* onClickListener= */ (dialog, which) -> {
                    DefaultTrackSelector.ParametersBuilder builder = parameters.buildUpon();
                    for (int i = 0; i < mappedTrackInfo.getRendererCount(); i++) {
                        builder
                                .clearSelectionOverrides(/* rendererIndex= */ i)
                                .setRendererDisabled(
                                        /* rendererIndex= */ i,
                                        trackSelectionDialog.getIsDisabled(/* rendererIndex= */ i));
                        List<SelectionOverride> overrides =
                                trackSelectionDialog.getOverrides(/* rendererIndex= */ i);
                        if (!overrides.isEmpty()) {
                            builder.setSelectionOverride(
                                    /* rendererIndex= */ i,
                                    mappedTrackInfo.getTrackGroups(/* rendererIndex= */ i),
                                    overrides.get(0));
                        }
                    }
                    trackSelector.setParameters(builder);
                    AppController.childQaulity=childQuality;
                    AppController.childQaulityAudioVideo=childQualityAudio;
                    AppController.childQaulityText=childQualityText;

                    SessionManager.setAudioVideo(AppController.getInstance(),childQualityAudio);
                    SessionManager.setVideoQuality(AppController.getInstance(),childQuality);
                    SessionManager.setAudioText(AppController.getInstance(),childQualityText);
                   // AppController.getInstance().builder=builder;
                },
                onDismissListener);
        return trackSelectionDialog;
    }

    /**
     * Creates a dialog for given {@link MappedTrackInfo} and {@link DefaultTrackSelector.Parameters}.
     *
     * @param titleId The resource id of the dialog title.
     * @param mappedTrackInfo The {@link MappedTrackInfo} to display.
     * @param initialParameters The {@link DefaultTrackSelector.Parameters} describing the initial
     *     track selection.
     * @param allowAdaptiveSelections Whether adaptive selections (consisting of more than one track)
     *     can be made.
     * @param allowMultipleOverrides Whether tracks from multiple track groups can be selected.
     * @param onClickListener {@link DialogInterface.OnClickListener} called when tracks are selected.
     * @param onDismissListener {@link DialogInterface.OnDismissListener} called when the dialog is
     *     dismissed.
     */
    public static TrackSelectionDialog createForMappedTrackInfoAndParameters(
            int titleId,
            MappedTrackInfo mappedTrackInfo,
            DefaultTrackSelector.Parameters initialParameters,
            boolean allowAdaptiveSelections,
            boolean allowMultipleOverrides,
            DialogInterface.OnClickListener onClickListener,
            DialogInterface.OnDismissListener onDismissListener) {
        TrackSelectionDialog trackSelectionDialog = new TrackSelectionDialog();
        trackSelectionDialog.init(
                titleId,
                mappedTrackInfo,
                initialParameters,
                allowAdaptiveSelections,
                allowMultipleOverrides,
                onClickListener,
                onDismissListener);
        return trackSelectionDialog;
    }

    public TrackSelectionDialog() {
        tabFragments = new SparseArray<>();
        tabTrackTypes = new ArrayList<>();
        // Retain instance across activity re-creation to prevent losing access to init data.
        setRetainInstance(true);
    }

    private void init(
            int titleId,
            MappedTrackInfo mappedTrackInfo,
            DefaultTrackSelector.Parameters initialParameters,
            boolean allowAdaptiveSelections,
            boolean allowMultipleOverrides,
            DialogInterface.OnClickListener onClickListener,
            DialogInterface.OnDismissListener onDismissListener) {
        this.titleId = titleId;
        this.onClickListener = onClickListener;
        this.onDismissListener = onDismissListener;
        for (int i = 0; i < mappedTrackInfo.getRendererCount(); i++) {
            if (showTabForRenderer(mappedTrackInfo, i)) {
                int trackType = mappedTrackInfo.getRendererType(/* rendererIndex= */ i);
                TrackGroupArray trackGroupArray = mappedTrackInfo.getTrackGroups(i);
                TrackSelectionViewFragment tabFragment = new TrackSelectionViewFragment();
                tabFragment.init(
                        mappedTrackInfo,
                        /* rendererIndex= */ i,
                        initialParameters.getRendererDisabled(/* rendererIndex= */ i),
                        initialParameters.getSelectionOverride(/* rendererIndex= */ i, trackGroupArray),
                        false,
                        allowMultipleOverrides);
                tabFragments.put(i, tabFragment);
                tabTrackTypes.add(trackType);
            }
        }
    }

    /**
     * Returns whether a renderer is disabled.
     *
     * @param rendererIndex Renderer index.
     * @return Whether the renderer is disabled.
     */
    public boolean getIsDisabled(int rendererIndex) {
        TrackSelectionViewFragment rendererView = tabFragments.get(rendererIndex);
        return rendererView != null && rendererView.isDisabled;
    }

    /**
     * Returns the list of selected track selection overrides for the specified renderer. There will
     * be at most one override for each track group.
     *
     * @param rendererIndex Renderer index.
     * @return The list of track selection overrides for this renderer.
     */
    public List<SelectionOverride> getOverrides(int rendererIndex) {
        TrackSelectionViewFragment rendererView = tabFragments.get(rendererIndex);
        return rendererView == null ? Collections.emptyList() : rendererView.overrides;
    }
    static String video_quality;
    static String languages;
    static String subtitles;
    static String Best;
    static String Basic;
    static String average;
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // We need to own the view to let tab layout work correctly on all API levels. We can't use
        // AlertDialog because it owns the view itself, so we use AppCompatDialog instead, themed using
        // the AlertDialog theme overlay with force-enabled title.
        AppCompatDialog dialog =
                new AppCompatDialog(getActivity(), R.style.TrackSelectionDialogThemeOverlay);
        dialog.setTitle(titleId);
         video_quality=getString(R.string.video_quality);
        languages=getString(R.string.languages);
        subtitles=getString(R.string.subtitles);
        Best=getString(R.string.Best);
        Basic=getString(R.string.Basic);
        average=getString(R.string.average);
        return dialog;
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
        onDismissListener.onDismiss(dialog);
    }

    @Nullable
    @Override
    public View onCreateView(
            LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View dialogView = inflater.inflate(R.layout.track_selection_dialog, container, false);
        TabLayout tabLayout = dialogView.findViewById(R.id.track_selection_dialog_tab_layout);
        ViewPager viewPager = dialogView.findViewById(R.id.track_selection_dialog_view_pager);
        Button cancelButton = dialogView.findViewById(R.id.track_selection_dialog_cancel_button);
        Button okButton = dialogView.findViewById(R.id.track_selection_dialog_ok_button);
        viewPager.setAdapter(new FragmentAdapter(getChildFragmentManager()));
        tabLayout.setupWithViewPager(viewPager);


        tabLayout.setVisibility(tabFragments.size() > 1 ? View.VISIBLE : View.GONE);
        cancelButton.setOnClickListener(view -> dismiss());
        okButton.setOnClickListener(
                view -> {
                    onClickListener.onClick(getDialog(), DialogInterface.BUTTON_POSITIVE);
                    dismiss();
                });
        return dialogView;
    }

    private static boolean showTabForRenderer(MappedTrackInfo mappedTrackInfo, int rendererIndex) {
        TrackGroupArray trackGroupArray = mappedTrackInfo.getTrackGroups(rendererIndex);
        if (trackGroupArray.length == 0) {
            return false;
        }
        int trackType = mappedTrackInfo.getRendererType(rendererIndex);
        return isSupportedTrackType(trackType);
    }

    private static boolean isSupportedTrackType(int trackType) {
        switch (trackType) {
            case C.TRACK_TYPE_VIDEO:
            case C.TRACK_TYPE_AUDIO:
            case C.TRACK_TYPE_TEXT:
                return true;
            default:
                return false;
        }
    }

    private static String getTrackTypeString(Resources resources, int trackType) {
        switch (trackType) {
            case C.TRACK_TYPE_VIDEO:
                return /*resources.getString(R.string.exo_track_selection_title_video)*/video_quality;
            case C.TRACK_TYPE_AUDIO:
                return /*resources.getString(R.string.exo_track_selection_title_audio)*/languages;
            case C.TRACK_TYPE_TEXT:
                return /*resources.getString(R.string.exo_track_selection_title_text)*/subtitles;
            default:
                throw new IllegalArgumentException();
        }
    }

    private final class FragmentAdapter extends FragmentPagerAdapter {

        public FragmentAdapter(FragmentManager fragmentManager) {
            super(fragmentManager);
        }

        @Override
        public Fragment getItem(int position) {
            return tabFragments.valueAt(position);
        }

        @Override
        public int getCount() {
            return tabFragments.size();
        }

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            return getTrackTypeString(getResources(), tabTrackTypes.get(position));
        }
    }

    /** Fragment to show a track seleciton in tab of the track selection dialog. */
    public static final class TrackSelectionViewFragment extends Fragment
            implements TrackSelectionView.TrackSelectionListener {

        private MappedTrackInfo mappedTrackInfo;
        private int rendererIndex;
        private boolean allowAdaptiveSelections;
        private boolean allowMultipleOverrides;

        /* package */ boolean isDisabled;
        /* package */ List<SelectionOverride> overrides;

        public TrackSelectionViewFragment() {
            // Retain instance across activity re-creation to prevent losing access to init data.
            setRetainInstance(true);
        }

        public void init(
                MappedTrackInfo mappedTrackInfo,
                int rendererIndex,
                boolean initialIsDisabled,
                @Nullable SelectionOverride initialOverride,
                boolean allowAdaptiveSelections,
                boolean allowMultipleOverrides) {
            this.mappedTrackInfo = mappedTrackInfo;
            this.rendererIndex = rendererIndex;
            this.isDisabled = initialIsDisabled;
            this.overrides =
                    initialOverride == null
                            ? Collections.emptyList()
                            : Collections.singletonList(initialOverride);
            this.allowAdaptiveSelections = allowAdaptiveSelections;
            this.allowMultipleOverrides = allowMultipleOverrides;
        }

        @Nullable
        @Override
        public View onCreateView(
                LayoutInflater inflater,
                @Nullable ViewGroup container,
                @Nullable Bundle savedInstanceState) {
            View rootView =
                    inflater.inflate(
                            R.layout.exo_track_selection_dialog, container, /* attachToRoot= */ false);
            TrackSelectionView trackSelectionView = rootView.findViewById(R.id.exo_track_selection_view);
            trackSelectionView.setShowDisableOption(true);
            trackSelectionView.setAllowMultipleOverrides(allowMultipleOverrides);
            trackSelectionView.setAllowAdaptiveSelections(allowAdaptiveSelections);
            trackSelectionView.init(
                    mappedTrackInfo, rendererIndex, isDisabled, overrides, /* listener= */ this);
            try {



                int pos=-1;
                int auto_pos=-1;
                for(int k=0;k<trackSelectionView.getChildCount();k++)
                {
                    View childview= trackSelectionView.getChildAt(k);
                    if(childview instanceof CheckedTextView)
                    {

                        String s=((CheckedTextView)childview).getText().toString();

                        if(s.contains("Unknown"))
                        {
                            pos=k;
                        }

                        if(s.contains(", Stereo"))
                        {
                            s=s.replace(", Stereo","");
                            ((CheckedTextView) childview).setText(s);
                        }


                        if(s.contains("640 × 360"))
                        {
                            s=(Best);
                            ((CheckedTextView) childview).setText(s);
                            if(AppController.childQaulity==0)
                                ((CheckedTextView)trackSelectionView.getChildAt(k)).setChecked(true);

                        }

                        if(s.contains("854 × 480"))
                        {
                            s=(average);
                            ((CheckedTextView) childview).setText(s);
                            if(AppController.childQaulity==1)
                                ((CheckedTextView)trackSelectionView.getChildAt(k)).setChecked(true);
                        }

                        if(s.contains("1280 × 720"))
                        {
                            s=(Best);
                            ((CheckedTextView) childview).setText(s);
                            if(AppController.childQaulity==2)
                                ((CheckedTextView)trackSelectionView.getChildAt(k)).setChecked(true);
                        }

                        if(s.contains("Auto")&&rendererIndex==0&&trackSelectionView.getChildAt(k)!=null) {
                            ((CheckedTextView) trackSelectionView.getChildAt(k)).setChecked(false);

                            if (AppController.getInstance().childQaulity == -1)
                                ((CheckedTextView) trackSelectionView.getChildAt(k)).setChecked(true);
                        }



                            if(s.contains("English")&&rendererIndex==1&&trackSelectionView.getChildAt(k)!=null) {
                                ((CheckedTextView) trackSelectionView.getChildAt(k)).setChecked(false);

                                if (AppController.getInstance().childQaulityAudio == 0)
                                    ((CheckedTextView) trackSelectionView.getChildAt(k)).setChecked(true);
                            }

                            if(s.contains("Hindi")&&rendererIndex==1&&trackSelectionView.getChildAt(k)!=null) {
                            ((CheckedTextView) trackSelectionView.getChildAt(k)).setChecked(false);

                            if (AppController.getInstance().childQaulityAudio == 1)
                                ((CheckedTextView) trackSelectionView.getChildAt(k)).setChecked(true);
                             }


                        if(s.contains("None")&&rendererIndex==2&&trackSelectionView.getChildAt(k)!=null) {
                            ((CheckedTextView) trackSelectionView.getChildAt(k)).setChecked(false);

                            if (AppController.getInstance().childQaulityText == 0||AppController.getInstance().childQaulityText == -1)
                                ((CheckedTextView) trackSelectionView.getChildAt(k)).setChecked(true);
                        }

                        if(s.contains("English")&&rendererIndex==2&&trackSelectionView.getChildAt(k)!=null) {
                            ((CheckedTextView) trackSelectionView.getChildAt(k)).setChecked(false);

                            if (AppController.getInstance().childQaulityText == 1)
                                ((CheckedTextView) trackSelectionView.getChildAt(k)).setChecked(true);
                        }
                    }


                }
                // view.setText("Hide");
                if(pos>-1&&trackSelectionView.getChildCount()>pos)
                {

                    trackSelectionView.removeViewAt(pos);
                }

                if(rendererIndex==0)
                {
                    trackSelectionView.removeViewAt(0);
                }else if(rendererIndex==1)
                {
                    auto_pos=-1;

                    for(int k=0;k<trackSelectionView.getChildCount();k++)
                    {
                        View childview= trackSelectionView.getChildAt(k);
                        if(childview instanceof CheckedTextView)
                        {


                            String s=((CheckedTextView)childview).getText().toString();

                            if(s.contains("Auto"))
                            {
                                auto_pos=k;
                            }


                        }


                    }
                    // view.setText("Hide");
                    if(auto_pos>-1&&trackSelectionView.getChildCount()>auto_pos)
                    {

                        trackSelectionView.removeViewAt(auto_pos);
                    }


                        trackSelectionView.removeViewAt(0);
                }else if(rendererIndex==2)
                {

                    auto_pos=-1;

                    for(int k=0;k<trackSelectionView.getChildCount();k++)
                    {
                        View childview= trackSelectionView.getChildAt(k);
                        if(childview instanceof CheckedTextView)
                        {


                            String s=((CheckedTextView)childview).getText().toString();
                            if(s.contains("Auto"))
                            {
                                auto_pos=k;
                            }

                        }


                    }
                    // view.setText("Hide");
                    if(auto_pos>-1&&trackSelectionView.getChildCount()>auto_pos)
                    {

                        trackSelectionView.removeViewAt(auto_pos);
                    }
                    View noneView= trackSelectionView.getChildAt(0);
                    if(noneView instanceof CheckedTextView)
                    {
                        String s=((CheckedTextView)noneView).getText().toString();
                        if(s.contains("None")) {
                            ((CheckedTextView) noneView).setText("Off");

                        }
                    }

                }




            }catch (Exception e)
            {
                e.printStackTrace();
            }
            return rootView;
        }

        @Override
        public void onTrackSelectionChanged(boolean isDisabled, List<DefaultTrackSelector.SelectionOverride> overrides) {
            this.isDisabled = isDisabled;
            this.overrides = overrides;

            try{


            if(this.rendererIndex==0)
            {
                childQuality=0;
                if(overrides.size()>0)
                {

                    SelectionOverride ocr= overrides.get(0);
                    if(ocr!=null)
                    {
                        if(ocr.containsTrack(0))
                        {
                            childQuality=0;
                        }else   if(ocr.containsTrack(1))
                        {
                            childQuality=1;
                        }else   if(ocr.containsTrack(2))
                        {
                            childQuality=2;
                        }else   if(ocr.containsTrack(3))
                        {
                            childQuality=3;
                        }
                    }

                }else
                {
                    childQuality=0;
                }
            }else if(rendererIndex==1)
            {
                childQualityAudio=-1;
                if(overrides.size()>0)
                {

                    SelectionOverride ocr= overrides.get(0);
                    for(int k=0;k<ocr.tracks.length;k++)

                        if(overrides.get(0).groupIndex==0)
                        {
                            childQualityAudio=0;
                        }else   if(overrides.get(0).groupIndex==1)
                        {
                            childQualityAudio=1;
                        }


                }else
                {
                    childQualityAudio=-1;
                }
            }
            else if(rendererIndex==2)
            {
                childQualityText=-1;
                if(overrides.size()>0)
                {

                    SelectionOverride ocr= overrides.get(0);

                    for(int k=0;k<ocr.tracks.length;k++)

                        if(overrides.get(0).groupIndex==0)
                        {
                            childQualityText=0;
                        }else   if(overrides.get(0).groupIndex==1)
                        {
                            childQualityText=1;
                        }


                }else
                {
                    childQualityText=0;
                }
            }
            }catch (Exception e)
            {
                e.printStackTrace();
            }

        }
    }
}
