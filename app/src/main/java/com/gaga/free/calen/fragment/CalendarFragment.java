package com.gaga.free.calen.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.gaga.free.R;
import com.gaga.free.calen.CustomCalendar;
import com.gaga.free.calen.Singleton;
import com.gaga.free.calen.adapter.CalendarGridviewAdapter;
import com.gaga.free.calen.dao.CalendarDecoratorDao;
import com.gaga.free.calen.dao.CalendarResponse;
import com.gaga.free.calen.dao.Event;
import com.gaga.free.calen.dao.EventData;
import com.gaga.free.calen.utils.CalendarUtils;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;

public class CalendarFragment extends Fragment {
    private static final String TAG = CalendarFragment.class.getSimpleName();

    private GridView mGridview;
    private LinearLayout mLlDayList;
    private RelativeLayout mRlHeader;
    private GregorianCalendar month;
    private CalendarGridviewAdapter mCalendarGridviewAdapter;
    private boolean flagMaxMin = false;
    public static String currentDateSelected;
    private Calendar mCalendar;
    private DateFormat mDateFormat;
    private GregorianCalendar mPMonth;
    private int mMonthLength;
    private GregorianCalendar mPMonthMaxSet;
    private ArrayList<CalendarDecoratorDao> mEventList = new ArrayList<>();
    private ViewGroup rootView;
    private static CustomCalendar mCustomCalendar;


    public static CalendarFragment newInstance(CustomCalendar calendar) {
        CalendarFragment fragment = new CalendarFragment();
        fragment.setCalendar(calendar);
        return fragment;
    }


    private void setCalendar(CustomCalendar calendar) {
        mCustomCalendar = calendar;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        rootView = (ViewGroup) inflater.inflate(
                R.layout.fragement, container, false);

        initCurrentMonthInGridview();

        if (Singleton.getInstance().getIsSwipeViewPager() == 2)
            refreshDays();

        return rootView;
    }


    private void initCurrentMonthInGridview() {

        mLlDayList = (LinearLayout) rootView.findViewById(R.id.llDayList);
        mRlHeader = (RelativeLayout) rootView.findViewById(R.id.rlMonthTitle);

        month = Singleton.getInstance().getMonth();

        mCalendarGridviewAdapter = new CalendarGridviewAdapter(getActivity(), mEventList, month);

        mCalendar = month;
        mCalendar.set(Calendar.DAY_OF_MONTH, 1);
        mCalendar.setFirstDayOfWeek(Calendar.SUNDAY);
        mDateFormat = CalendarUtils.getCalendarDBFormat();

        mGridview = (GridView) rootView.findViewById(R.id.gvCurrentMonthDayList);
        mGridview.setAdapter(mCalendarGridviewAdapter);

        TextView title = (TextView) rootView.findViewById(R.id.title);
        title.setText(android.text.format.DateFormat.format(CalendarUtils.getCalendarMonthTitleFormat(), month));

        mGridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {

                String selectedDate = mEventList.get(position).getDate();
                ((CalendarGridviewAdapter) parent.getAdapter()).setSelected(v, selectedDate);
                fetchEvents(selectedDate);

            }
        });
    }


    public void fetchEvents(String date) {
        boolean flag = false;
        int pos = 0;
        for (int i = 0; i < Singleton.getInstance().getEventManager().size(); i++) {
            if (Singleton.getInstance().getEventManager().get(i).getDate().equalsIgnoreCase(date)) {
                flag = true;
                pos = i;
            }
        }
        ArrayList<EventData> eventDataArrayList = new ArrayList();
        if (flag) {
            if (Singleton.getInstance().getEventManager().get(pos).getEventData() != null) {
                eventDataArrayList = Singleton.getInstance().getEventManager().get(pos).getEventData();
            }
        }

        if (mCustomCalendar != null)
            mCustomCalendar.setDateSelectionData(eventDataArrayList);
    }


    public void refreshCalendar() {
        TextView title = (TextView) rootView.findViewById(R.id.title);
        refreshDays();
        title.setText(android.text.format.DateFormat.format(CalendarUtils.getCalendarMonthTitleFormat(), month));
    }


    public void refreshDays() {

        //clear List
        mEventList.clear();
        //create clone
        mPMonth = (GregorianCalendar) mCalendar.clone();

        CalendarGridviewAdapter.firstDay = mCalendar.get(GregorianCalendar.DAY_OF_WEEK);

        int mMaxWeekNumber = mCalendar.getActualMaximum(Calendar.WEEK_OF_MONTH);

        mMonthLength = mMaxWeekNumber * 7;
        int mMaxP = getmMaxP();
        int mCalMaxP = mMaxP - (CalendarGridviewAdapter.firstDay - 1);

        mPMonthMaxSet = (GregorianCalendar) mPMonth.clone();

        mPMonthMaxSet.set(GregorianCalendar.DAY_OF_MONTH, mCalMaxP + 1);

        setData(getCalendarData());

    }


    private CalendarResponse getCalendarData() {
        CalendarResponse calendarResponse = new CalendarResponse();
        calendarResponse.setStartmonth(Singleton.getInstance().getStartMonth());
        calendarResponse.setEndmonth(Singleton.getInstance().getEndMonth());
        calendarResponse.setMonthdata(Singleton.getInstance().getEventManager());
        return calendarResponse;
    }


    private void setData(CalendarResponse calendarResponse) {

        mLlDayList.setVisibility(View.VISIBLE);
        mRlHeader.setVisibility(View.VISIBLE);
        mGridview.setVisibility(View.VISIBLE);

        if (calendarResponse.getMonthdata() != null) {

            ArrayList<Event> monthDataList = calendarResponse.getMonthdata();
            int m = 0;

            for (int n = 0; n < mMonthLength; n++) {
                String mItemValue = mDateFormat.format(mPMonthMaxSet.getTime());
                mPMonthMaxSet.add(GregorianCalendar.DATE, 1);

                if (m < monthDataList.size()) {
                    if (mItemValue.equalsIgnoreCase(monthDataList.get(m).getDate())) {
                        CalendarDecoratorDao eventDao = new CalendarDecoratorDao(
                                monthDataList.get(m).getDate(),
                                Integer.parseInt(monthDataList.get(m).getCount()));
                        mEventList.add(eventDao);
                        m++;
                    } else {
                        CalendarDecoratorDao eventDao = new CalendarDecoratorDao(mItemValue, 0);
                        mEventList.add(eventDao);
                    }
                } else {
                    CalendarDecoratorDao eventDao = new CalendarDecoratorDao(mItemValue, 0);
                    mEventList.add(eventDao);
                }
            }

            mCalendarGridviewAdapter.notifyDataSetChanged();

            if (!flagMaxMin) {
                flagMaxMin = true;
            }
        }
    }



    public void setNextMonth() {
        if (month.get(GregorianCalendar.MONTH) == month
                .getActualMaximum(GregorianCalendar.MONTH)) {
            month.set((month.get(GregorianCalendar.YEAR) + 1),
                    month.getActualMinimum(GregorianCalendar.MONTH), 1);
            Singleton.getInstance().setMonth(month);
        } else {
            month.set(GregorianCalendar.MONTH,
                    month.get(GregorianCalendar.MONTH) + 1);
            Singleton.getInstance().setMonth(month);
        }
    }


    public void setPreviousMonth() {
        if (month.get(GregorianCalendar.MONTH) == month
                .getActualMinimum(GregorianCalendar.MONTH)) {
            month.set((month.get(GregorianCalendar.YEAR) - 1),
                    month.getActualMaximum(GregorianCalendar.MONTH), 1);
            Singleton.getInstance().setMonth(month);
        } else {
            month.set(GregorianCalendar.MONTH,
                    month.get(GregorianCalendar.MONTH) - 1);
            Singleton.getInstance().setMonth(month);
        }
    }


    private int getmMaxP() {
        int maxP;
        if (mCalendar.get(GregorianCalendar.MONTH) == mCalendar
                .getActualMinimum(GregorianCalendar.MONTH)) {
            mPMonth.set((mCalendar.get(GregorianCalendar.YEAR) - 1),
                    mCalendar.getActualMaximum(GregorianCalendar.MONTH), 1);
        } else {
            mPMonth.set(GregorianCalendar.MONTH,
                    mCalendar.get(GregorianCalendar.MONTH) - 1);
        }
        maxP = mPMonth.getActualMaximum(GregorianCalendar.DAY_OF_MONTH);

        return maxP;
    }

}
