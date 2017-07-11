/**
 * 
 */
package com.aptina.camera.fragments;

import java.util.List;

import org.achartengine.ChartFactory;
import org.achartengine.GraphicalView;
import org.achartengine.chart.PointStyle;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.model.XYSeries;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;

import android.app.Fragment;
import android.graphics.Color;
import android.graphics.Paint.Align;
import android.os.Bundle;
import android.support.v13.app.FragmentPagerAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.aptina.R;
import com.aptina.adapter.HistogramPageAdapter;
import com.aptina.data.HistogramRGBData;

/**
 * A fragment class that is called from the {@link FragmentPagerAdapter}, and with the
 * supportv13 library is inserted in to a ViewPager. Each fragment can display all 3 of the
 * RGB channels, or any fraction of them
 * 
 * @author stoyan
 */
public class HistogramFrag extends Fragment {
	/**
	 * Logging tag
	 */
	private static final String TAG = "HistogramFrag";
	
	/**
	 * Strings for setting arguments of the fragment from {@link #setArguments(Bundle)}
	 */
	public static final String EXTRA_KEY_TITLE = "EXTRA_CHART_TITLE";
	public static final String EXTRA_VALUE_ALL = "RGB Histogram";
	public static final String EXTRA_VALUE_RED = "Red Histogram";
	public static final String EXTRA_VALUE_GREEN = "Green Histogram";
	public static final String EXTRA_VALUE_BLUE = "Blue Histogram";
	private String mHistogramTitle = EXTRA_VALUE_ALL;
	
	/**
	 * Strings for setting arguments of the fragment from {@link #setArguments(Bundle)}
	 */
	public static final String EXTRA_CHART_LIST_SIZE = "CHART_LIST_SIZE";
	public static final int DEFAULT_CHART_LIST_SIZE = 1;
	
	/**
	 * The size of the chart represents how many color channels are graphed on each fragment,
	 * RGB would be 3, Red only would be 1
	 */
	private int mChartListSize;
	
	/**
	 * A list of array of doubles, must be same size as {@link #mYAxisList} and each
	 * double[] must be same length as its {@link #mYAxisList} companion. Both must be the
	 * same size as {@link #mChartListSize}
	 */
	private List<double[]> mXAxisList;
	
	/**
	 * A list of array of doubles, must be same size as {@link #mXAxisList} and each
	 * double[] must be same length as its {@link #mXAxisList} companion. Both must be the
	 * same size as {@link #mChartListSize}
	 */
	private List<double[]> mYAxisList;
	
	/**
	 * Strings for setting arguments of the fragment from {@link #setArguments(Bundle)}
	 */
	public static final String EXTRA_COLOR_CHANNEL = "EXTRA_COLOR_CHANNEL";
	
	/**
	 * Available legend names for each color channel
	 */
	private static final String[] HISTOGRAM_LEGEND_TITLES = new String[] { "Red", "Green", "Blue"};
	
	/**
	 * Legend titles to be graphed, must be of same size as {@link #mChartListSize}
	 */
	private String[] mGraphedLegendTitles = null;
	
	/**
	 * Available legend colors for each color channel
	 */
	private static final int[] HISTOGRAM_LEGEND_COLORS = new int[] {Color.RED, Color.GREEN, Color.BLUE};
	
	/**
	 * Legend colors to be graphed, must be of same size as {@link #mChartListSize}
	 */
	private int[] mGraphedLegendColors = null;
	
	private static final PointStyle[] HISTOGRAM_LEGEND_STYLES = new PointStyle[] { PointStyle.CIRCLE, PointStyle.DIAMOND, PointStyle.TRIANGLE};
	
	/**
	 * Legend point styles to be graphed, must be of same size as {@link #mChartListSize}
	 */
	private PointStyle[] mGraphedLegendStyles = null;
	/**
	 * The chart view to draw
	 */
	private GraphicalView mChart;
	
	/** 
	 * The main dataset that includes all the series that go into a chart.
	 */
	private XYMultipleSeriesDataset mDataset = new XYMultipleSeriesDataset();
	
	/** 
	 * The main renderer that includes all the renderers customizing a chart. 
	 */
	private XYMultipleSeriesRenderer mRenderer = new XYMultipleSeriesRenderer();
	
	
	/**
	 * Instantiate the fragment with the {@link #setArguments(Bundle)} called 
	 * @param title 
	 * @param chartSets
	 * @param colorChannel
	 * @return
	 */
	public static final HistogramFrag newInstance(String title, int chartSets, int colorChannel)
	{
		HistogramFrag frag = new HistogramFrag();
		Bundle bundle = new Bundle(3);
		bundle.putString(EXTRA_KEY_TITLE, title);
		bundle.putInt(EXTRA_CHART_LIST_SIZE, chartSets);
		bundle.putInt(EXTRA_COLOR_CHANNEL, colorChannel);
	    frag.setArguments(bundle);
	    return frag;
	}
	
	/**
	 * Set all the arguments for the fragment and initialize the graph
	 */
	@Override
	public void setArguments(Bundle bundle) {
		if(bundle != null){
        	Log.i(TAG, "setArguments extra color : " + bundle.getString(EXTRA_KEY_TITLE, EXTRA_VALUE_ALL));
        	mHistogramTitle = bundle.getString(EXTRA_KEY_TITLE, EXTRA_VALUE_ALL);
        	mChartListSize = bundle.getInt(EXTRA_CHART_LIST_SIZE, DEFAULT_CHART_LIST_SIZE);
        	
        	try {
				setGraphedChannels(mHistogramTitle, mChartListSize);
			} catch (Exception e) {
				Log.e(TAG, "mChartListSize != mHistogramTitle");
				e.printStackTrace();
			}
        	
        }
	}
	
	
	/**
	 * Call this function in {@link #setArguments(Bundle)}, function sets the graph with
	 * axis scale and data from the singleton {@link HistogramRGBData}
	 * 
	 * @param histTitle The graph's main title
	 * @param chartSize The number of color channels to be displayed
	 * @throws IllegalArgumentException Thrown if number of channels does not equal the number of legend functions
	 */
	private void setGraphedChannels(String histTitle, int chartSize) throws IllegalArgumentException{
		mGraphedLegendTitles = new String[1];
		mGraphedLegendColors = new int[1];
		mGraphedLegendStyles = new PointStyle[1];
		if(histTitle.equals(EXTRA_VALUE_RED)){
			mGraphedLegendTitles[0] = HISTOGRAM_LEGEND_TITLES[0];
			mGraphedLegendColors[0] = HISTOGRAM_LEGEND_COLORS[0];
			mGraphedLegendStyles[0] = HISTOGRAM_LEGEND_STYLES[0];
			mYAxisList = HistogramRGBData.getInstance().getYAxisList(HistogramRGBData.RED_CHANNEL);
		}else if(histTitle.equals(EXTRA_VALUE_GREEN)){
			mGraphedLegendTitles[0] = HISTOGRAM_LEGEND_TITLES[1];
			mGraphedLegendColors[0] = HISTOGRAM_LEGEND_COLORS[1];
			mGraphedLegendStyles[0] = HISTOGRAM_LEGEND_STYLES[1];
			mYAxisList = HistogramRGBData.getInstance().getYAxisList(HistogramRGBData.GREEN_CHANNEL);
		}else if(histTitle.equals(EXTRA_VALUE_BLUE)){
			mGraphedLegendTitles[0] = HISTOGRAM_LEGEND_TITLES[2];
			mGraphedLegendColors[0] = HISTOGRAM_LEGEND_COLORS[2];
			mGraphedLegendStyles[0] = HISTOGRAM_LEGEND_STYLES[2];
			mYAxisList = HistogramRGBData.getInstance().getYAxisList(HistogramRGBData.BLUE_CHANNEL);
		}else{
			mGraphedLegendTitles = HISTOGRAM_LEGEND_TITLES;
			mGraphedLegendColors = HISTOGRAM_LEGEND_COLORS;
			mGraphedLegendStyles = HISTOGRAM_LEGEND_STYLES;
			mYAxisList = HistogramRGBData.getInstance().getYAxisList(HistogramRGBData.RGB_CHANNELS);
		}
		if(chartSize != mGraphedLegendTitles.length){
			throw new IllegalArgumentException("Chart size and histogram graph size must be equal");
		}
		mXAxisList = HistogramRGBData.getInstance().getXAxisList(chartSize);
		
	}
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        Log.d(TAG, "onCreate");
        
    }
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
//		Log.d(TAG, "onCreateView");
        // Inflate the layout for this fragment
		final View fragV = inflater.inflate(R.layout.histogram_frag, container, false);
		LinearLayout layout = (LinearLayout) fragV.findViewById(R.id.histogram_ll_main);
		mChart = createHistView();
		mChart.setBackgroundDrawable(getResources().getDrawable(R.drawable.round_histogram));
		layout.addView(mChart);
        return fragV;
    }
	@Override
    public void onResume() {
        super.onResume();
    }
	
	@Override
	public void onSaveInstanceState(Bundle outState){
		super.onSaveInstanceState(outState);
//		Log.d(TAG, "onSaveInstanceState");
	}
	
    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
    
    /**
     * Create the histogram as a line graph view that goes inside of ViewPager
     * @return The GraphicalView to add
     */
    private GraphicalView createHistView() {
		

    	mRenderer = buildRenderer(mGraphedLegendColors, mGraphedLegendStyles);
	    int length = mRenderer.getSeriesRendererCount();
	    for (int i = 0; i < length; i++) {
	      ((XYSeriesRenderer) mRenderer.getSeriesRendererAt(i)).setFillPoints(true);
	    }
	    setChartArguments(mRenderer);
	    mRenderer.setXLabels(12);
	    mRenderer.setYLabels(10);
	    mRenderer.setShowGrid(true);
	    mRenderer.setXLabelsAlign(Align.RIGHT);
	    mRenderer.setYLabelsAlign(Align.RIGHT);
	    mRenderer.setPanEnabled(false, false);
	    mRenderer.setZoomRate(1.1f);
	    mDataset = buildDataset(mGraphedLegendTitles, mXAxisList, mYAxisList);
	    return ChartFactory.getLineChartView(getActivity(),mDataset, mRenderer);
	}
	
    /**
     * Sets the arguments on the charts for each histogram color if the {@link #setArguments(Bundle)} was set
     * in the {@link HistogramPageAdapter}
     * 
     * @param renderer The renderer for the chart
     */
	private void setChartArguments(XYMultipleSeriesRenderer renderer) {
		setChartSettings(renderer, mHistogramTitle, "Intensity", "", 0,
				HistogramRGBData.CURRENT_CHART_INTENSITY_DEPTH, 0, 1, Color.GRAY, Color.LTGRAY);
		
	}
	

	/**
	   * Builds an XY multiple series renderer.
	   * 
	   * @param colors the series rendering colors
	   * @param styles the series point styles
	   * @return the XY multiple series renderers
	   */
	  protected XYMultipleSeriesRenderer buildRenderer(int[] colors, PointStyle[] styles) {
	    XYMultipleSeriesRenderer renderer = new XYMultipleSeriesRenderer();
	    setRenderer(renderer, colors, styles);
	    return renderer;
	  }

	  protected void setRenderer(XYMultipleSeriesRenderer renderer, int[] colors, PointStyle[] styles) {
	    renderer.setAxisTitleTextSize(16);
	    renderer.setChartTitleTextSize(20);
	    renderer.setLabelsTextSize(15);
	    renderer.setLegendTextSize(15);
	    renderer.setPointSize(5f);
	    renderer.setMargins(new int[] { 20, 30, 15, 20 });
	    int length = colors.length;
	    for (int i = 0; i < length; i++) {
	      XYSeriesRenderer r = new XYSeriesRenderer();
	      r.setColor(colors[i]);
//	      r.setPointStyle(styles[i]);
	      renderer.addSeriesRenderer(r);
	    }
	  }
	
	  /**
	   * Sets a few of the series renderer settings.
	   * 
	   * @param renderer the renderer to set the properties to
	   * @param title the chart title
	   * @param xTitle the title for the X axis
	   * @param yTitle the title for the Y axis
	   * @param xMin the minimum value on the X axis
	   * @param xMax the maximum value on the X axis
	   * @param yMin the minimum value on the Y axis
	   * @param yMax the maximum value on the Y axis
	   * @param axesColor the axes color
	   * @param labelsColor the labels color
	   */
	protected void setChartSettings(XYMultipleSeriesRenderer renderer, String title, String xTitle,
			String yTitle, double xMin, double xMax, double yMin, double yMax, int axesColor, int labelsColor) {
		renderer.setChartTitle(title);
	    renderer.setXTitle(xTitle);
	    renderer.setYTitle(yTitle);
	    renderer.setXAxisMin(xMin);
	    renderer.setXAxisMax(xMax);
	    renderer.setYAxisMin(yMin);
	    renderer.setYAxisMax(yMax);
	    renderer.setAxesColor(axesColor);
	    renderer.setLabelsColor(labelsColor);
	}
	
	
	  /**
	   * Builds an XY multiple dataset using the provided values.
	   * 
	   * @param titles the series titles
	   * @param xValues the values for the X axis
	   * @param yValues the values for the Y axis
	   * @return the XY multiple dataset
	   */
	  protected XYMultipleSeriesDataset buildDataset(String[] titles, List<double[]> xValues,
	      List<double[]> yValues) {
	    XYMultipleSeriesDataset dataset = new XYMultipleSeriesDataset();
	    addXYSeries(dataset, titles, xValues, yValues, 0);
	    return dataset;
	  }

	  public void addXYSeries(XYMultipleSeriesDataset dataset, String[] titles, List<double[]> xValues,
	      List<double[]> yValues, int scale) {
	          int length = titles.length;
//	          Log.d(TAG, "title lenght : " + length + ", xValues size : " + xValues.size() +  ", yValues size : " + yValues.size());
	          for (int i = 0; i < length; i++) {
	              XYSeries series = new XYSeries(titles[i], scale);
	              double[] xV = xValues.get(i);
	              double[] yV = yValues.get(i);
	              int seriesLength = xV.length;
	              for (int k = 0; k < seriesLength; k++) {
	                series.add(xV[k], yV[k]);
	              }
	              dataset.addSeries(series);
	          }
	  }
	  
	  
	  public void updateRedChannel(double[] yVal) throws IllegalArgumentException{
		  XYSeries series = mDataset.getSeriesAt(HistogramRGBData.RED_CHANNEL);
		  if(yVal.length != series.getItemCount()){
				throw new IllegalArgumentException("Size of update array does not match size of mDataset chart");
		  }
		  series.replaceYValues(yVal);
	  }
	  public void updateGreenChannel(double[] yVal) throws IllegalArgumentException{
		  XYSeries series = mDataset.getSeriesAt(HistogramRGBData.GREEN_CHANNEL);
		  if(yVal.length != series.getItemCount()){
				throw new IllegalArgumentException("Size of update array does not match size of mDataset chart");
		  }
		  series.replaceYValues(yVal);
	  }
	  public void updateBlueChannel(double[] yVal) throws IllegalArgumentException{
		  XYSeries series = mDataset.getSeriesAt(HistogramRGBData.BLUE_CHANNEL);
		  if(yVal.length != series.getItemCount()){
				throw new IllegalArgumentException("Size of update array does not match size of mDataset chart");
		  }
		  series.replaceYValues(yVal);
	  }
	  
	  public void repaintGraph(){
		  if(mChart != null){
//			  Log.i(TAG, "repaint");
//			  mChart.repaint();
		  }
	  }
	  
	  public String getHistogramTitle(){
		  return mHistogramTitle;
	  }

}
