package com.mgrmobi.brandbeat.ui.base;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

import com.mgrmobi.brandbeat.R;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * @author Vasiliy Yakimov
 *         Developed by Magora Team (magora-systems.com). 2016.
 */
public abstract class BaseActivity extends AppCompatActivity {
    protected Toolbar toolbar;

    @Bind(R.id.parent_view)
    protected View parentView;
    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(getLayoutId());
        ButterKnife.bind(this, this);
    }

    protected abstract int getLayoutId();

    protected View getParentView()
    {
        return parentView;
    }

    protected void showFragment(Fragment fragment) {

        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, fragment,
                fragment.getClass().getName()).commit();
    }
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
