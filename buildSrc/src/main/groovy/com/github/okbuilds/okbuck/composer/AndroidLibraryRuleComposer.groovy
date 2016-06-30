package com.github.okbuilds.okbuck.composer

import com.github.okbuilds.core.model.AndroidLibTarget
import com.github.okbuilds.core.model.AndroidTarget
import com.github.okbuilds.core.model.Target
import com.github.okbuilds.okbuck.generator.RetroLambdaGenerator
import com.github.okbuilds.okbuck.rule.AndroidLibraryRule

final class AndroidLibraryRuleComposer extends AndroidBuckRuleComposer {

    private AndroidLibraryRuleComposer() {
        // no instance
    }

    static AndroidLibraryRule compose(AndroidLibTarget target, List<String> deps,
                                      List<String> aptDeps, List<String> aidlRuleNames,
                                      String appClass) {
        deps.addAll(external(target.main.externalDeps))
        deps.addAll(targets(target.main.targetDeps))

        Set<String> providedDeps = []
        providedDeps.addAll(external(target.apt.externalDeps))
        providedDeps.addAll(targets(target.apt.targetDeps))
        providedDeps.removeAll(deps)

        target.main.targetDeps.each { Target targetDep ->
            if (targetDep instanceof AndroidTarget) {
                targetDep.resources.each { AndroidTarget.ResBundle bundle ->
                    deps.add(res(targetDep as AndroidTarget, bundle))
                }
            }
        }

        List<String> postprocessClassesCommands = []
        if (target.retrolambda) {
            postprocessClassesCommands.add(RetroLambdaGenerator.generate(target))
        }
        Set<String> srcSet = target.main.sources
        if (target.sqldelight) {
            srcSet.addAll(target.sqldelight.sources)
            if (srcSet.size() > 1) {
                throw new IllegalStateException("SqlDelight model module must not contain java dir in main source set")
            }
        }

        return new AndroidLibraryRule(
                src(target),
                ["PUBLIC"],
                deps,
                srcSet,
                target.manifest,
                target.annotationProcessors as List,
                aptDeps,
                providedDeps,
                aidlRuleNames,
                appClass,
                target.sourceCompatibility,
                target.targetCompatibility,
                postprocessClassesCommands,
                target.main.jvmArgs)
    }
}
