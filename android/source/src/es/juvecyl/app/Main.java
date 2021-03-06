
package es.juvecyl.app;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Locale;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;

import es.juvecyl.app.navs.MainNav;
import es.juvecyl.app.navs.MainNavMaker;
import es.juvecyl.app.utils.GifView;
import es.juvecyl.app.utils.LodgingSingleton;
import es.juvecyl.app.utils.XMLDB;

public class Main extends SherlockActivity {
    private float scale;
    private String targetProvince, provinceColor;
    private MainNavMaker arrayAdapter;
    private XMLDB db;
    private int counter = 0;
    private TextView[] tv;
    private Context actualcontext;
    private ArrayList<MainNav> navdata = new ArrayList<MainNav>();
    private Vibrator vibe;
    // //////////////////////////////////////////////////////////////////////////
    // CAPAS DEL XML
    // //////////////////////////////////////////////////////////////////////////
    private RelativeLayout contentFrame;
    private DrawerLayout drawerLayout;
    private ListView navList;
    // ESCUCHADOR DEL MENU NAV
    private ActionBarDrawerToggle navToggle;
    // //////////////////////////////////////////////////////////////////////////
    // CAPAS DE LA ACTIVIDAD
    // //////////////////////////////////////////////////////////////////////////
    private ScrollView scrollMain;
    private LinearLayout linearInsideScroll;
    private TextView provinceTextName;
    private EditText searchField;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left);
        setContentView(R.layout.provinces_layout);
        // //////////////////////////////////////////////////////////////////////////
        // PARA QUE LOS PIXELS SEAN EN FUNCIÓN DE LA DENSIDAD DE LA PANTALLA
        // //////////////////////////////////////////////////////////////////////////
        scale = getResources().getDisplayMetrics().density;
        Bundle bundle = this.getIntent().getExtras();
        targetProvince = bundle.getString("province");
        actualcontext = this;
        // //////////////////////////////////////////////////////////////////////////
        // RECOGEMOS DEL XML
        // //////////////////////////////////////////////////////////////////////////
        contentFrame = (RelativeLayout) findViewById(R.id.main_content_frame);
        drawerLayout = (DrawerLayout) findViewById(R.id.main_drawer_layout);
        navList = (ListView) findViewById(R.id.main_menu_drawer);
        // //////////////////////////////////////////////////////////////////////////
        // ESTABLECEMOS UN TÍTULO
        // //////////////////////////////////////////////////////////////////////////
        setTitle(targetProvince);
        // //////////////////////////////////////////////////////////////////////////
        // CARGAMOS LOS DATOS
        // //////////////////////////////////////////////////////////////////////////
        db = LodgingSingleton.getInstance(getApplicationContext()).getDB();
        // //////////////////////////////////////////////////////////////////////////
        // DECLARAMOS EL VIBRADOR
        // //////////////////////////////////////////////////////////////////////////
        vibe = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);

        // //////////////////////////////////////////////////////////////////////////
        // NAVEGADOR LATERAL
        //
        // PRIMERO HACEMOS QUE EL TÍTULO SEA UN BOTÓN QUE PUEDA EXPANDIR
        // EL MENÚ LATERAL
        //
        // //////////////////////////////////////////////////////////////////////////
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // //////////////////////////////////////////////////////////////////////////
        // DECLARAMOS EL ESCUCHADOR DE NUESTRO MENÚ LATERAL
        // //////////////////////////////////////////////////////////////////////////
        navToggle = new ActionBarDrawerToggle(this, drawerLayout,
                R.drawable.ic_drawer, R.string.open_drawer,
                R.string.close_drawer) {
            public void onDrawerClosed(View view) {
                invalidateOptionsMenu();
            }

            public void onDrawerOpened(View view) {
                invalidateOptionsMenu();
            }
        };
        drawerLayout.setDrawerListener(navToggle);

        // //////////////////////////////////////////////////////////////////////////
        // DECLARAMOS NUESTRO ARRAY DE NAVEGACIÓN PRINCIPAL Y COGEMOS
        // EL COLOR DE LA PROVINCIA
        // //////////////////////////////////////////////////////////////////////////
        navdata = new MainNav(getApplicationContext()).getNav();
        if (targetProvince.equals(
                getApplicationContext().getText(R.string.coding_search))) {
            Search();
            provinceColor = navdata.get(0).getBgColor();
        } else {
            Province();
        }

        // //////////////////////////////////////////////////////////////////////////
        // DECLARAMOS UN SCROLLVIEW PARA LOS RESULTADOS
        // //////////////////////////////////////////////////////////////////////////
        scrollMain = new ScrollView(actualcontext);
        scrollMain.setId(1989);
        RelativeLayout.LayoutParams scrollParams = new RelativeLayout.LayoutParams(
                LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        scrollParams.setMargins(0, (int) (10 * scale + 0.5f), 0, 0);
        scrollParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT,
                RelativeLayout.TRUE);
        scrollParams.addRule(RelativeLayout.BELOW, 1988);
        scrollMain.setLayoutParams(scrollParams);
        // //////////////////////////////////////////////////////////////////////////
        // DECLARAMOS EL LINEAR LAYOUT INTERNO AL SCROLLVIEW
        // //////////////////////////////////////////////////////////////////////////
        linearInsideScroll = new LinearLayout(actualcontext);
        linearInsideScroll.setId(1990);
        LinearLayout.LayoutParams linearInsideParams = new LinearLayout.LayoutParams(
                LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        linearInsideParams.topMargin = (int) (40 * scale + 0.5f);
        linearInsideScroll.setOrientation(LinearLayout.VERTICAL);
        linearInsideScroll.setLayoutParams(linearInsideParams);

        // //////////////////////////////////////////////////////////////////////////
        // CON TODAS LAS CAPAS DECLARADAS LAS AGREGAMOS A LA VISTA
        // //////////////////////////////////////////////////////////////////////////
        if (targetProvince.equals(
                getApplicationContext().getText(R.string.coding_search))) {
            contentFrame.addView(searchField);
            contentFrame.addView(scrollMain);
            scrollMain.addView(linearInsideScroll);
        } else {
            contentFrame.addView(provinceTextName);
            ProvinceResults();
        }
        // //////////////////////////////////////////////////////////////////////////
        // CREANDO EL NAVEGADOR LATERAL
        // //////////////////////////////////////////////////////////////////////////
        navdata = new MainNav(getApplicationContext()).getNav();
        arrayAdapter = new MainNavMaker(this, R.layout.main_list, navdata);
        navList.setAdapter(arrayAdapter);
        navList.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> a, View v, int pos, long id) {
                vibe.vibrate(60);
                String selected = ((MainNav) a.getAdapter().getItem(pos))
                        .getTitle();
                if (selected.equals(targetProvince)) {
                    // NOTHING
                } else {
                    targetProvince = selected;
                    setTitle(targetProvince);
                    ClearLayout();
                    if (!targetProvince.equals(
                            getApplicationContext().getText(R.string.coding_search))) {
                        ProvinceResults();
                    }
                    drawerLayout.closeDrawer(navList);
                }

            }
        });
    }

    // //////////////////////////////////////////////////////////////////////////
    // AÑADIMOS ELEMENTOS AL MENÚ DEL ACTION BAR
    // //////////////////////////////////////////////////////////////////////////
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getSupportMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    @SuppressLint("NewApi")
    @SuppressWarnings("deprecation")
    protected void Search() {
        // //////////////////////////////////////////////////////////////////////////
        // DECLARAMOS UN TEXTFIELD PARA BÚSQUEDAS
        // //////////////////////////////////////////////////////////////////////////
        searchField = new EditText(actualcontext);
        searchField.setId(1988);
        searchField.setSingleLine(true);
        RelativeLayout.LayoutParams editTextParams = new RelativeLayout.LayoutParams(
                LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        editTextParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
        editTextParams.topMargin = (int) (15 * scale + 0.5f);

        searchField.setPadding((int) (10 * scale + 0.5f),
                (int) (10 * scale + 0.5f), 0, (int) (5 * scale + 0.5f));
        searchField.setLayoutParams(editTextParams);
        searchField.setGravity(Gravity.START);
        searchField
                .setFilters(new InputFilter[] {
                        new InputFilter.LengthFilter(15)
                });
        searchField.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                vibe.vibrate(60);
            }
        });
        // //////////////////////////////////////////////////////////////////////////
        // COMPROBAMOS LA VERSIÓN DE ANDROID PARA USAR LA PROPIEDAD DEPRECADA
        // O LA NUEVA SI EL TARGET ES COMO MÍNIMO DE JELLY BEAN
        // //////////////////////////////////////////////////////////////////////////
        int sdk = android.os.Build.VERSION.SDK_INT;
        if (sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
            searchField.setBackgroundDrawable(getResources().getDrawable(
                    R.drawable.search_box));
        } else {
            searchField.setBackground(getResources().getDrawable(
                    R.drawable.search_box));
        }
        // //////////////////////////////////////////////////////////////////////////
        // ESCUCHADOR PARA CUANDO ESCRIBAMOS EN LA CAJA DE TEXTO
        // //////////////////////////////////////////////////////////////////////////
        searchField.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence arg0, int arg1, int arg2,
                    int arg3) {
                String searchString = searchField.getText().toString();
                // //////////////////////////////////////////////////////////////////////////
                // COMO MÍNIMO ESPERAMOS A 2 CARACTERES DE ENTRADA
                // //////////////////////////////////////////////////////////////////////////
                if (searchString.length() > 2) {
                    scrollMain.removeAllViews();
                    scrollMain.addView(linearInsideScroll);
                    linearInsideScroll.removeAllViews();
                    SearchResults(searchString);
                } else {
                    scrollMain.removeAllViews();
                    scrollMain.addView(linearInsideScroll);
                    linearInsideScroll.removeAllViews();
                }
            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1,
                    int arg2, int arg3) {
                // NO SE USA
            }

            @Override
            public void afterTextChanged(Editable arg0) {
                // NO SE USA

            }
        });
    }

    // //////////////////////////////////////////////////////////////////////////
    // BÚSQUEDA DE ALOJAMIENTOS POR NOMBRE
    // //////////////////////////////////////////////////////////////////////////
    protected void SearchResults(String searchString) {
        counter = 0;
        // //////////////////////////////////////////////////////////////////////////
        // BUSCAMOS COINCIDENCIAS Y DESPUÉS INSTANCIAMOS TANTOS TEXTVIEW
        // COMO COINCIDENCIAS HAYA
        // //////////////////////////////////////////////////////////////////////////
        for (int i = 0; i < db.getLodgings().size(); i++) {
            if (db.getLodgings().get(i).getTitle().toLowerCase(Locale.ENGLISH)
                    .contains(searchString.toLowerCase(Locale.ENGLISH))) {
                counter++;
            }
        }
        // //////////////////////////////////////////////////////////////////////////
        // SI NO HAY RESULTADOS MOSTRAMOS UN MENSAJE INFORMATIVO
        // //////////////////////////////////////////////////////////////////////////
        if (counter == 0) {
            // //////////////////////////////////////////////////////////////////////////
            // LAYOUT RELATIVO PARA EL MENSAJE
            // //////////////////////////////////////////////////////////////////////////
            scrollMain.removeAllViews();
            RelativeLayout relative = new RelativeLayout(actualcontext);
            RelativeLayout.LayoutParams relativeInsideParams = new RelativeLayout.LayoutParams(
                    LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
            relative.setLayoutParams(relativeInsideParams);

            // //////////////////////////////////////////////////////////////////////////
            // METEMOS AL EXCURSIONISTA
            // //////////////////////////////////////////////////////////////////////////
            @SuppressWarnings("unused")
            InputStream stream = null;
            try {
                stream = getAssets().open("excur.gif");
            } catch (Exception e) {
                Log.e("fallo fichero", e.toString());
            }
            GifView tripper = new GifView(actualcontext,
                    "file:///android_asset/excur.gif");
            tripper.setId(2009);
            RelativeLayout.LayoutParams relativeWeb = new RelativeLayout.LayoutParams(
                    (int) (300 * scale + 0.5f), (int) (300 * scale + 0.5f));
            relativeWeb.addRule(RelativeLayout.CENTER_HORIZONTAL);
            relativeWeb.setMargins((int) (10 * scale + 0.5f), 0, 0, 0);
            tripper.setLayoutParams(relativeWeb);
            tripper.setScrollContainer(false);
            tripper.setInitialScale((int) (100 * scale + 0.5f));

            // //////////////////////////////////////////////////////////////////////////
            // METEMOS EL TEXTO
            // //////////////////////////////////////////////////////////////////////////
            TextView results = new TextView(actualcontext);
            RelativeLayout.LayoutParams textParams = new RelativeLayout.LayoutParams(
                    LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
            textParams.addRule(RelativeLayout.CENTER_HORIZONTAL,
                    RelativeLayout.TRUE);
            textParams.setMargins(0, (int) (10 * scale + 0.5f), 0,
                    (int) (10 * scale + 0.5f));
            textParams.addRule(RelativeLayout.BELOW, 2009);
            results.setLayoutParams(textParams);
            results.setText(getResources().getString(R.string.main_no_results));
            // //////////////////////////////////////////////////////////////////////////
            // AÑADIMOS TODO AL LAYOUT
            // //////////////////////////////////////////////////////////////////////////
            scrollMain.addView(relative);
            relative.addView(tripper);
            relative.addView(results);

        } else {
            tv = new TextView[counter];
            counter = 0;
            // //////////////////////////////////////////////////////////////////////////
            // METEMOS LAS COINCIDENCIAS EN SENDOS TEXTVIEW
            // //////////////////////////////////////////////////////////////////////////
            for (int i = 0; i < db.getLodgings().size(); i++) {
                if (db.getLodgings().get(i).getTitle()
                        .toLowerCase(Locale.ENGLISH)
                        .contains(searchString.toLowerCase(Locale.ENGLISH))) {
                    tv[counter] = new TextView(actualcontext);
                    LinearLayout.LayoutParams prm = new LinearLayout.LayoutParams(
                            LayoutParams.MATCH_PARENT,
                            LayoutParams.WRAP_CONTENT);
                    prm.setMargins(0, (int) (5 * scale + 0.5f), 0,
                            (int) (5 * scale + 0.5f));
                    tv[counter].setLayoutParams(prm);
                    tv[counter].setText(db.getLodgings().get(i).getTitle());
                    for (int j = 0; j < navdata.size(); j++) {
                        if (navdata.get(j).getTitle()
                                .equals(db.getLodgings().get(i).getProvince())) {
                            tv[counter].setBackgroundColor(Color
                                    .parseColor(navdata.get(j).getBgColor()));
                            break;
                        }
                    }
                    tv[counter].setTextColor(Color.WHITE);
                    tv[counter].setPadding((int) (10 * scale + 0.5f),
                            (int) (20 * scale + 0.5f),
                            (int) (10 * scale + 0.5f),
                            (int) (20 * scale + 0.5f));
                    tv[counter].setTextSize(TypedValue.COMPLEX_UNIT_DIP, 18);
                    tv[counter].setTypeface(null, Typeface.BOLD);
                    tv[counter].setTag((Object) i);
                    linearInsideScroll.addView(tv[counter]);
                    tv[counter].setOnClickListener(new OnClickListener() {

                        @Override
                        public void onClick(View v) {
                            vibe.vibrate(60);
                            Intent intent = new Intent(Main.this, LodgingDetail.class);
                            Bundle b = new Bundle();
                            b.putString("lodging", v.getTag().toString());
                            intent.putExtras(b);
                            startActivity(intent);
                        }
                    });
                    counter++;
                }
            }
        }
    }

    protected void Province() {
        for (int i = 0; i < navdata.size(); i++) {
            if (navdata.get(i).getTitle().equals(targetProvince)) {
                provinceColor = navdata.get(i).getBgColor();
                break;
            }
        }
        // //////////////////////////////////////////////////////////////////////////
        // DECLARAMOS UN TEXTVIEW
        // //////////////////////////////////////////////////////////////////////////
        provinceTextName = new TextView(actualcontext);
        provinceTextName.setId(1988);
        RelativeLayout.LayoutParams textViewParams = new RelativeLayout.LayoutParams(
                LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        provinceTextName.setLayoutParams(textViewParams);
        provinceTextName.setGravity(Gravity.CENTER);
        provinceTextName.setText(targetProvince);
        provinceTextName.setTextSize(getResources().getDimension(
                R.dimen.small_font));
        provinceTextName.setTextColor(getResources().getColor(R.color.white));
        try{
            provinceTextName.setBackgroundColor(Color.parseColor(provinceColor));
        }
        catch (Exception e){
            Log.e("fail", "color fail");
        }
        provinceTextName.setTextSize(TypedValue.COMPLEX_UNIT_SP, 25);
        provinceTextName.setTypeface(null, Typeface.BOLD);
    }

    protected void ProvinceResults() {
        for (int i = 0; i < navdata.size(); i++) {
            if (navdata.get(i).getTitle().equals(targetProvince)) {
                provinceColor = navdata.get(i).getBgColor();
                break;
            }
        }
        // //////////////////////////////////////////////////////////////////////////
        // MOSTRAR RESULTADOS DE LA PROVINCIA
        // //////////////////////////////////////////////////////////////////////////
        counter = 0;
        Log.d("Error: ", db.getLodgings().size() + "");
        for (int i = 0; i < db.getLodgings().size(); i++) {
            if (db.getLodgings().get(i).getProvince().equals(targetProvince)) {
                counter++;
            }
        }
        tv = new TextView[counter];
        counter = 0;
        for (int i = 0; i < db.getLodgings().size(); i++) {
            if (db.getLodgings().get(i).getProvince().equals(targetProvince)) {
                tv[counter] = new TextView(actualcontext);
                LinearLayout.LayoutParams prm = new LinearLayout.LayoutParams(
                        LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
                prm.setMargins(0, (int) (5 * scale + 0.5f), 0,
                        (int) (5 * scale + 0.5f));
                tv[counter].setLayoutParams(prm);
                tv[counter].setText(db.getLodgings().get(i).getTitle());
                tv[counter].setBackgroundColor(Color.parseColor(provinceColor));
                tv[counter].setTextColor(Color.WHITE);
                tv[counter].setPadding((int) (10 * scale + 0.5f),
                        (int) (20 * scale + 0.5f), (int) (10 * scale + 0.5f),
                        (int) (20 * scale + 0.5f));
                tv[counter].setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
                tv[counter].setTag((Object) i);
                tv[counter].setTypeface(null, Typeface.BOLD);

                tv[counter].setOnClickListener(new OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        vibe.vibrate(60);
                        Intent intent = new Intent(Main.this, LodgingDetail.class);
                        Bundle b = new Bundle();
                        b.putString("lodging", v.getTag().toString());
                        intent.putExtras(b);
                        startActivity(intent);
                    }
                });

                linearInsideScroll.addView(tv[counter]);
                counter++;
            }
        }
    }

    protected void ClearLayout() {
        contentFrame.removeAllViews();
        if (targetProvince.equals(
                getApplicationContext().getString(R.string.coding_search))) {
            Search();
            contentFrame.addView(searchField);
        } else {
            Province();
            contentFrame.addView(provinceTextName);
        }
        contentFrame.addView(scrollMain);
        scrollMain.removeAllViews();
        scrollMain.addView(linearInsideScroll);
        linearInsideScroll.removeAllViews();
    }

    public boolean onMenuItemSelected(int featureId, MenuItem item) {

        int itemId = item.getItemId();
        switch (itemId) {
            case android.R.id.home:
                vibe.vibrate(60);
                if (drawerLayout.isDrawerOpen(navList)) {
                    drawerLayout.closeDrawer(navList);
                } else {
                    drawerLayout.openDrawer(navList);
                }
                break;
            case R.id.favs:
                vibe.vibrate(60);
                startActivity(new Intent(this, Favs.class));
                break;
            case R.id.reload:
                vibe.vibrate(60);
                startActivity(new Intent(this, DownloadXML.class));
                finish();
                break;
        }

        return true;
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        navToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        navToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            moveTaskToBack(true);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
