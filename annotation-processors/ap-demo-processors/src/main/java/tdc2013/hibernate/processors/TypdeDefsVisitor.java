/*
 * Copyright (c) 2013, Klaus Boeing & Michel Graciano.
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * Redistributions of source code must retain the above copyright notice, this
 * list of conditions and the following disclaimer.
 *
 * Redistributions in binary form must reproduce the above copyright notice,
 * this list of conditions and the following disclaimer in the documentation
 * and/or other materials provided with the distribution.
 *
 * Neither the name of the project's authors nor the names of its contributors
 * may be used to endorse or promote products derived from this software without
 * specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDERS AND/OR CONTRIBUTORS
 * BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 */

package tdc2013.hibernate.processors;

import java.util.Collections;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.AnnotationValue;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.PackageElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.TypeParameterElement;
import javax.lang.model.element.VariableElement;

public class TypdeDefsVisitor extends AbstractElementVisitor<Set<String>, Void> {

    public TypdeDefsVisitor(ProcessingEnvironment processingEnv) {
        super(processingEnv);
    }

    @Override
    public Set<String> visitPackage(PackageElement e, Void p) {
        final TypdeDefsInfo info = new TypdeDefsInfo(e);

        Logger.getGlobal().log(Level.WARNING, "PackageElement {0}...", e.toString());
        for (AnnotationMirror am : e.getAnnotationMirrors()) {
            if (!am.getAnnotationType().asElement().getSimpleName().contentEquals("TypeDefs")) {
                continue;
            }

            for (Map.Entry<? extends ExecutableElement, ? extends AnnotationValue> entry : am.getElementValues().
                    entrySet()) {
                final AnnotationValue typeDef = entry.getValue();
                typeDef.accept(new TypeDefNameVisitor(), info);
                typeDef.accept(new TypeDefEnumClassVisitor(processingEnv), info);
            }
        }

        Logger.getGlobal().log(Level.WARNING, "Names {0}...", info.toString());
        return info.getTypesNames();
    }

    @Override
    public Set<String> visitType(TypeElement e, Void p) {
        Logger.getGlobal().log(Level.WARNING, "TypeElement {0}...", e.toString());
        return Collections.emptySet();
    }

    @Override
    public Set<String> visitVariable(VariableElement e, Void p) {
        Logger.getGlobal().log(Level.WARNING, "VariableElement {0}...", e.toString());
        return Collections.emptySet();
    }

    @Override
    public Set<String> visitExecutable(ExecutableElement e, Void p) {
        Logger.getGlobal().log(Level.WARNING, "ExecutableElement {0}...", e.toString());
        return Collections.emptySet();
    }

    @Override
    public Set<String> visitTypeParameter(TypeParameterElement e, Void p) {
        Logger.getGlobal().log(Level.WARNING, "TypeParameterElement {0}...", e.toString());
        return Collections.emptySet();
    }
}
