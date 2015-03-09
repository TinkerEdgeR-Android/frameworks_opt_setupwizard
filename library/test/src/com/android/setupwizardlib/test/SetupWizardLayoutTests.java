/*
 * Copyright (C) 2015 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.android.setupwizardlib.test;

import android.annotation.TargetApi;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build.VERSION_CODES;
import android.test.InstrumentationTestCase;
import android.test.suitebuilder.annotation.SmallTest;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.android.setupwizardlib.SetupWizardLayout;
import com.android.setupwizardlib.view.NavigationBar;

public class SetupWizardLayoutTests extends InstrumentationTestCase {

    @SmallTest
    public void testDefaultTemplate() {
        SetupWizardLayout layout = new SetupWizardLayout(getInstrumentation().getContext());
        assertDefaultTemplateInflated(layout);
    }

    @SmallTest
    public void testSetHeaderText() {
        SetupWizardLayout layout = new SetupWizardLayout(getInstrumentation().getContext());
        TextView title = (TextView) layout.findViewById(R.id.suw_layout_title);
        layout.setHeaderText("Abracadabra");
        assertEquals("Header text should be \"Abracadabra\"", "Abracadabra", title.getText());
    }

    @TargetApi(VERSION_CODES.JELLY_BEAN_MR1)
    @SmallTest
    public void testAddView() {
        SetupWizardLayout layout = new SetupWizardLayout(getInstrumentation().getContext());
        TextView tv = new TextView(getInstrumentation().getContext());
        int id = View.generateViewId();
        tv.setId(id);
        layout.addView(tv);
        assertDefaultTemplateInflated(layout);
        View view = layout.findViewById(id);
        assertSame("The view added should be the same text view", tv, view);
    }

    @SmallTest
    public void testInflateFromXml() {
        LayoutInflater inflater = LayoutInflater.from(getInstrumentation().getContext());
        SetupWizardLayout layout = (SetupWizardLayout) inflater.inflate(R.layout.test_layout, null);
        assertDefaultTemplateInflated(layout);
        View content = layout.findViewById(R.id.test_content);
        assertTrue("@id/test_content should be a TextView", content instanceof TextView);
    }

    @TargetApi(VERSION_CODES.JELLY_BEAN_MR1)
    @SmallTest
    public void testCustomTemplate() {
        SetupWizardLayout layout = new SetupWizardLayout(getInstrumentation().getContext(),
                R.layout.test_template);
        View templateView = layout.findViewById(R.id.test_template_view);
        assertNotNull("@id/test_template_view should exist in template", templateView);

        TextView tv = new TextView(getInstrumentation().getContext());
        int id = View.generateViewId();
        tv.setId(id);
        layout.addView(tv);

        templateView = layout.findViewById(R.id.test_template_view);
        assertNotNull("@id/test_template_view should exist in template", templateView);
        View contentView = layout.findViewById(id);
        assertSame("The view added should be the same text view", tv, contentView);

        // The following methods should be no-ops because the custom template doesn't contain the
        // corresponding optional views. Just check that they don't throw exceptions.
        layout.setHeaderText("Abracadabra");
        layout.setIllustration(new ColorDrawable(Color.MAGENTA));
        layout.setLayoutBackground(new ColorDrawable(Color.RED));
    }

    private void assertDefaultTemplateInflated(SetupWizardLayout layout) {
        View decorView = layout.findViewById(R.id.suw_layout_decor);
        View navbar = layout.findViewById(R.id.suw_layout_navigation_bar);
        View title = layout.findViewById(R.id.suw_layout_title);
        assertNotNull("@id/suw_layout_decor_view should not be null", decorView);
        assertTrue("@id/suw_layout_navigation_bar should be an instance of NavigationBar",
                navbar instanceof NavigationBar);
        assertNotNull("@id/suw_layout_title should not be null", title);
    }
}
