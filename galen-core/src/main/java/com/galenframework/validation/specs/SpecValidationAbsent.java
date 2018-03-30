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

import static java.lang.String.format;

import com.galenframework.specs.SpecAbsent;
import com.galenframework.validation.*;
import com.galenframework.page.PageElement;

public class SpecValidationAbsent extends SpecValidation<SpecAbsent> {

    @Override
    public ValidationResult check(PageValidation pageValidation, String objectName, SpecAbsent spec) throws ValidationErrorException {
        PageElement mainObject = pageValidation.findPageElement(objectName);
        if (mainObject != null && mainObject.isPresent() && mainObject.isVisible()) {
            throw new ValidationErrorException()
                .withValidationObject(new ValidationObject(mainObject.getArea(), objectName))
                .withMessage(format("\"%s\" is not absent on page", objectName));
        }

        return new ValidationResult(spec);
    }

}
