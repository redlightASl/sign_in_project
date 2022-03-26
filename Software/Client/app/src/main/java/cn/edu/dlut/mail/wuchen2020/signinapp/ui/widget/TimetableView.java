package cn.edu.dlut.mail.wuchen2020.signinapp.ui.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.ColorInt;
import androidx.annotation.Dimension;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.edu.dlut.mail.wuchen2020.signinapp.R;
import cn.edu.dlut.mail.wuchen2020.signinapp.model.Course;
import cn.edu.dlut.mail.wuchen2020.signinapp.model.LessonTime;

public class TimetableView extends LinearLayout {
    private String[] daysOfWeek;
    @ColorInt
    private int[] courseColors;

    private int weekHeight;
    private int periodWidth;
    private int cellHeight;
    private float courseRadius;
    private int dividerSize;
    private int weekTextSize;
    private int periodTextSize;
    private int courseTextSize;
    @ColorInt
    private int titleColor;
    @ColorInt
    private int titleBackgroundColor;

    // 每节课的时间
    private List<LessonTime> timeList;
    // 课程原始数据
    private List<Course> courseList;
    // 按照星期几存储并排好序的课程数据
    private final Map<Integer, List<Course>> courseMap = new HashMap<>();
    // 课程的背景颜色缓存
    private final Map<String, Integer> colorMap = new HashMap<>();

    // 更新课程时需要重绘的布局
    private LinearLayout layoutMain;
    // 课程点击事件监听器
    private OnCourseClickListener onCourseClickListener;

    public TimetableView(Context context) {
        this(context, null);
    }

    public TimetableView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TimetableView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        daysOfWeek = context.getResources().getStringArray(R.array.week);
        courseColors = context.getResources().getIntArray(R.array.course_colors);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.TimetableView);
        weekHeight = a.getDimensionPixelSize(R.styleable.TimetableView_weekHeight, dp2px(30));
        periodWidth = a.getDimensionPixelSize(R.styleable.TimetableView_periodWidth, dp2px(30));
        cellHeight = a.getDimensionPixelSize(R.styleable.TimetableView_cellHeight, dp2px(60));
        courseRadius = a.getDimension(R.styleable.TimetableView_courseRadius, dp2px(8));
        dividerSize = a.getDimensionPixelSize(R.styleable.TimetableView_dividerSize, dp2px(0.75f));
        weekTextSize = a.getDimensionPixelSize(R.styleable.TimetableView_weekTextSize, 12);
        periodTextSize = a.getDimensionPixelSize(R.styleable.TimetableView_periodTextSize, 8);
        courseTextSize = a.getDimensionPixelSize(R.styleable.TimetableView_courseTextSize, 12);
        titleColor = a.getColor(R.styleable.TimetableView_titleColor, Color.BLACK);
        titleBackgroundColor = a.getColor(R.styleable.TimetableView_titleBackgroundColor, Color.WHITE);
        a.recycle();
        setOrientation(VERTICAL);
        LinearLayout layoutTitle = new LinearLayout(context);
        layoutTitle.setOrientation(HORIZONTAL);
        layoutTitle.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, weekHeight));
        layoutTitle.setBackgroundColor(titleBackgroundColor);
        // 表头左侧空白
        View space = new TextView(context);
        space.setLayoutParams(new ViewGroup.LayoutParams(periodWidth, ViewGroup.LayoutParams.MATCH_PARENT));
        layoutTitle.addView(space);
        // 星期
        for (String day : daysOfWeek) {
            View divider = createVerticalDivider();
            layoutTitle.addView(divider);
            TextView textDay = createTextView(0, ViewGroup.LayoutParams.MATCH_PARENT, 1, day, weekTextSize, titleColor);
            layoutTitle.addView(textDay);
        }
        addView(layoutTitle);
        View divider = createHorizontalDivider();
        addView(divider);
        // 初始化主布局
        layoutMain = new LinearLayout(context);
        layoutMain.setOrientation(HORIZONTAL);
        layoutMain.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        addView(layoutMain);
        // 正在加载中
        TextView textLoading = createTextView(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT,
                1, "正在加载中...", 24, Color.GRAY);
        textLoading.setPadding(0, dp2px(80), 0, 0);
        layoutMain.addView(textLoading);
    }

    public void setTimeList(List<LessonTime> times) {
        timeList = times;
    }

    public void setCourseList(List<Course> courses) {
        courseList = courses;
    }

    private void parseCourses() {
        courseMap.clear();
        for (int day = 1; day <= daysOfWeek.length; day++) {
            // TODO 替换成filter (没用是因为需要API24)
            List<Course> courses = new ArrayList<>();
            for (Course course : courseList) {
                if (course.getDayOfWeek() == day)
                    courses.add(course);
            }
            Collections.sort(courses, (o1, o2) -> o1.getStartTime() - o2.getStartTime());
            courseMap.put(day, courses);
        }
    }

    public void refreshView() {
        if (timeList == null || courseList == null) return;
        parseCourses();
        layoutMain.removeAllViews();
        // 节数
        LinearLayout layoutTitle = new LinearLayout(getContext());
        layoutTitle.setOrientation(VERTICAL);
        layoutTitle.setLayoutParams(new LayoutParams(periodWidth, ViewGroup.LayoutParams.WRAP_CONTENT));
        layoutTitle.setBackgroundColor(titleBackgroundColor);
        for (LessonTime time : timeList) {
            String text = String.format("%s\n%s\n%s", time.getPeriod(), time.getStartTime().substring(0, 5), time.getEndTime().substring(0, 5));
            TextView textPeriod = createTextView(ViewGroup.LayoutParams.MATCH_PARENT, cellHeight, 1, text, periodTextSize, titleColor);
            layoutTitle.addView(textPeriod);
            View divider = createHorizontalDivider();
            layoutTitle.addView(divider);
        }
        layoutMain.addView(layoutTitle);
        // 课程信息
        for (int i = 1; i <= daysOfWeek.length; i++) {
            View divider = createVerticalDivider();
            layoutMain.addView(divider);
            LinearLayout layoutCourses = createDayCourses(courseMap.get(i));
            layoutMain.addView(layoutCourses);
        }
        invalidate();
    }

    private TextView createTextView(int width, int height, int weight, String text, int textSize, int color) {
        TextView textView = new TextView(getContext());
        textView.setLayoutParams(new LayoutParams(width, height, weight));
        textView.setGravity(Gravity.CENTER);
        textView.setText(text);
        textView.setTextSize(textSize);
        textView.setTextColor(color);
        return textView;
    }

    private View createHorizontalDivider() {
        View view = new View(getContext());
        view.setLayoutParams(new ViewGroup.LayoutParams(LayoutParams.MATCH_PARENT, dividerSize));
        view.setBackgroundColor(getResources().getColor(android.R.color.darker_gray));
        return view;
    }

    private View createVerticalDivider() {
        View view = new View(getContext());
        view.setLayoutParams(new ViewGroup.LayoutParams(dividerSize, ViewGroup.LayoutParams.MATCH_PARENT));
        view.setBackgroundColor(getResources().getColor(android.R.color.darker_gray));
        return view;
    }

    private View createBlankCell() {
        View view = new TextView(getContext());
        view.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, cellHeight));
        return view;
    }

    private void addBlankCells(ViewGroup parent, int count) {
        for (int i = 0; i < count; i++) {
            View cell = createBlankCell();
            parent.addView(cell);
            View divider = createHorizontalDivider();
            parent.addView(divider);
        }
    }

    private int getCourseColor(Course course) {
        Integer cache = colorMap.get(course.getName());
        if (cache != null) {
            return cache;
        } else {
            int color = courseColors[colorMap.size() % courseColors.length];
            colorMap.put(course.getName(), color);
            return color;
        }
    }

    private View createCourseCell(Course course) {
        TextView textView = new TextView(getContext());
        int lastPeriods = course.getEndTime() - course.getStartTime() + 1;
        int height = cellHeight * lastPeriods + dividerSize * (lastPeriods - 1);
        textView.setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, height));
        textView.setGravity(Gravity.CENTER);
        textView.setText(String.format("%s\n%s", course.getName(), course.getLocation()));
        textView.setTextSize(courseTextSize);
        textView.setTextColor(Color.WHITE);
        textView.setOnClickListener(view -> onCourseClickListener.onCourseClick(course));
        GradientDrawable gradientDrawable = new GradientDrawable();
        gradientDrawable.setCornerRadius(courseRadius);
        gradientDrawable.setColor(getCourseColor(course));
        textView.setBackground(gradientDrawable);
        return textView;
    }

    private LinearLayout createDayCourses(List<Course> courses) {
        LinearLayout linearLayout = new LinearLayout(getContext());
        linearLayout.setLayoutParams(new LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1));
        linearLayout.setOrientation(VERTICAL);
        if (courses != null) {
            int i = 1;
            for (Course course : courses) {
                // 画这节课前面的空格
                addBlankCells(linearLayout, course.getStartTime() - i);
                // 画这节课
                View cell = createCourseCell(course);
                linearLayout.addView(cell);
                View divider = createHorizontalDivider();
                linearLayout.addView(divider);
                i = course.getEndTime() + 1;
            }
            // 画这天剩下的空格
            addBlankCells(linearLayout, timeList.size() - i + 1);
        } else {
            addBlankCells(linearLayout, timeList.size());
        }
        return linearLayout;
    }

    public int dp2px(@Dimension(unit = Dimension.DP) float dp) {
        DisplayMetrics metrics = getResources().getDisplayMetrics();
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, metrics);
    }

    public void setOnCourseClickListener(OnCourseClickListener listener) {
        if (!isClickable()) {
            setClickable(true);
        }
        onCourseClickListener = listener;
    }

    public interface OnCourseClickListener {
        void onCourseClick(Course course);
    }
}
