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
package com.galenframework.validation.specs;

import com.galenframework.specs.SpecVisible;
import com.galenframework.validation.*;
import com.galenframework.page.PageElement;

import static java.lang.String.format;
import static java.util.Arrays.asList;

public class SpecValidationVisible extends SpecValidation<SpecVisible> {

    @Override
    public ValidationResult check(PageValidation pageValidation, String objectName, SpecVisible spec) throws ValidationErrorException {
        PageElement mainObject = pageValidation.findPageElement(objectName);
        if (mainObject == null) {
            throw new ValidationErrorException(format(OBJECT_WITH_NAME_S_IS_NOT_DEFINED_IN_PAGE_SPEC, objectName));
        }
        if (!mainObject.isPresent()) {
            throw new ValidationErrorException(format(OBJECT_S_IS_ABSENT_ON_PAGE, objectName));
        } else if (!mainObject.isVisible()) {
            throw new ValidationErrorException((format(OBJECT_S_IS_NOT_VISIBLE_ON_PAGE, objectName)));
        }

        return new ValidationResult(spec, asList(new ValidationObject(mainObject.getArea(), objectName)));
    }

}
