/*******************************************************************************
* Copyright 2017 Ivan Shubin http://galenframework.com
* 
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at
* 
*   http://www.apache.org/licenses/LICENSE-2.0
* 
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
******************************************************************************/
package com.galenframework.validation;

import java.util.LinkedList;
import java.util.List;

import com.galenframework.specs.page.PageSection;
import com.galenframework.specs.Spec;
import com.galenframework.suite.GalenPageAction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CombinedValidationListener implements ValidationListener {

    private final static Logger LOG = LoggerFactory.getLogger(CombinedValidationListener.class);

    private List<ValidationListener> listeners = new LinkedList<>();
    
    
    @Override
    public void onObject(PageValidation pageValidation, String objectName) {
        for (ValidationListener listener : listeners) {
            try {
                listener.onObject(pageValidation, objectName);
            }
            catch (Exception ex) {
                LOG.error("Unknown error during finishing test", ex);
            }
        }
    }

    @Override
    public void onAfterObject(PageValidation pageValidation, String objectName) {
        for (ValidationListener listener: listeners) {
            try {
                listener.onAfterObject(pageValidation, objectName);
            }
            catch (Exception ex) {
                LOG.error("Unknown error during finishing test", ex);
            }
        }
    }

    @Override
    public void onBeforeSpec(PageValidation pageValidation, String objectName, Spec spec) {
        for (ValidationListener listener: listeners) {
            try {
                listener.onBeforeSpec(pageValidation, objectName, spec);
            }
            catch (Exception ex) {
                LOG.error("Unknown error during finishing test", ex);
            }
        }
    }

    @Override
    public void onSpecError(PageValidation pageValidation, String objectName, Spec spec, ValidationResult result) {
        for (ValidationListener listener: listeners) {
            try {
                listener.onSpecError(pageValidation, objectName, spec, result);
            }
            catch (Exception ex) {
                LOG.error("Unknown error during finishing test", ex);
            }
        }
    }


    @Override
    public void onSpecSuccess(PageValidation pageValidation, String objectName, Spec spec, ValidationResult result) {
        for (ValidationListener listener: listeners) {
            try {
                listener.onSpecSuccess(pageValidation, objectName, spec, result);
            }
            catch (Exception ex) {
                LOG.error("Unknown error during finishing test", ex);
            }
        }
    }

    @Override
    public void onGlobalError(Exception e) {
        for (ValidationListener listener: listeners) {
            try {
                listener.onGlobalError(e);
            }
            catch (Exception ex) {
                LOG.error("Unknown error during finishing test", ex);
            }
        }
    }

    @Override
    public void onBeforePageAction(GalenPageAction action) {
        for (ValidationListener listener: listeners) {
            try {
                listener.onBeforePageAction(action);
            }
            catch (Exception ex) {
                LOG.error("Unknown error during finishing test", ex);
            }
        }
    }

    @Override
    public void onAfterPageAction(GalenPageAction action) {
        for (ValidationListener listener: listeners) {
            try {
                listener.onAfterPageAction(action);
            }
            catch (Exception ex) {
                LOG.error("Unknown error during finishing test", ex);
            }
        }
    }

    @Override
    public void onBeforeSection(PageValidation pageValidation, PageSection pageSection) {
        for (ValidationListener listener: listeners) {
            try {
                listener.onBeforeSection(pageValidation, pageSection);
            }
            catch (Exception ex) {
                LOG.error("Unknown error during finishing test", ex);
            }
        }
    }

    @Override
    public void onAfterSection(PageValidation pageValidation, PageSection pageSection) {
        for (ValidationListener listener: listeners) {
            try {
                listener.onAfterSection(pageValidation, pageSection);
            }
            catch (Exception ex) {
                LOG.error("Unknown error during finishing test", ex);
            }
        }
    }

    @Override
    public void onSubLayout(PageValidation pageValidation, String objectName) {
        for (ValidationListener listener: listeners) {
            try {
                listener.onSubLayout(pageValidation, objectName);
            }
            catch (Exception ex) {
                LOG.error("Unknown error during finishing test", ex);
            }
        }
    }

    @Override
    public void onAfterSubLayout(PageValidation pageValidation, String objectName) {
        for (ValidationListener listener: listeners) {
            try {
                listener.onAfterSubLayout(pageValidation, objectName);
            }
            catch (Exception ex) {
                LOG.error("Unknown error during finishing test", ex);
            }
        }

    }

    @Override
    public void onSpecGroup(PageValidation pageValidation, String specGroupName) {
        for (ValidationListener listener: listeners) {
            try {
                listener.onSpecGroup(pageValidation, specGroupName);
            }
            catch (Exception ex) {
                LOG.error("Unknown error during finishing test", ex);
            }
        }
    }

    @Override
    public void onAfterSpecGroup(PageValidation pageValidation, String specGroupName) {
        for (ValidationListener listener: listeners) {
            try {
                listener.onAfterSpecGroup(pageValidation, specGroupName);
            }
            catch (Exception ex) {
                LOG.error("Unknown error during finishing test", ex);
            }
        }

    }

    public void add(ValidationListener validationListener) {
        if (validationListener != null) {
            listeners.add(validationListener);
        }
    }

}
