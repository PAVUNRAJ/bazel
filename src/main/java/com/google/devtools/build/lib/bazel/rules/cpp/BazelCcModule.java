// Copyright 2018 The Bazel Authors. All rights reserved.
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//    http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package com.google.devtools.build.lib.bazel.rules.cpp;

import com.google.devtools.build.lib.actions.Artifact;
import com.google.devtools.build.lib.analysis.platform.ConstraintValueInfo;
import com.google.devtools.build.lib.analysis.starlark.StarlarkActionFactory;
import com.google.devtools.build.lib.analysis.starlark.StarlarkRuleContext;
import com.google.devtools.build.lib.rules.cpp.CcCommon.Language;
import com.google.devtools.build.lib.rules.cpp.CcCompilationContext;
import com.google.devtools.build.lib.rules.cpp.CcCompilationOutputs;
import com.google.devtools.build.lib.rules.cpp.CcDebugInfoContext;
import com.google.devtools.build.lib.rules.cpp.CcLinkingContext;
import com.google.devtools.build.lib.rules.cpp.CcLinkingContext.LinkerInput;
import com.google.devtools.build.lib.rules.cpp.CcLinkingOutputs;
import com.google.devtools.build.lib.rules.cpp.CcModule;
import com.google.devtools.build.lib.rules.cpp.CcToolchainConfigInfo;
import com.google.devtools.build.lib.rules.cpp.CcToolchainProvider;
import com.google.devtools.build.lib.rules.cpp.CcToolchainVariables;
import com.google.devtools.build.lib.rules.cpp.CppModuleMap;
import com.google.devtools.build.lib.rules.cpp.CppSemantics;
import com.google.devtools.build.lib.rules.cpp.FdoContext;
import com.google.devtools.build.lib.rules.cpp.FeatureConfigurationForStarlark;
import com.google.devtools.build.lib.rules.cpp.LibraryToLink;
import com.google.devtools.build.lib.rules.cpp.LtoBackendArtifacts;
import com.google.devtools.build.lib.starlarkbuildapi.cpp.BazelCcModuleApi;
import net.starlark.java.eval.EvalException;
import net.starlark.java.eval.Sequence;
import net.starlark.java.eval.Starlark;
import net.starlark.java.eval.StarlarkInt;
import net.starlark.java.eval.StarlarkThread;

/**
 * A module that contains Starlark utilities for C++ support.
 *
 */
public class BazelCcModule extends CcModule
    implements BazelCcModuleApi<
        StarlarkActionFactory,
        Artifact,
        FdoContext,
        ConstraintValueInfo,
        StarlarkRuleContext,
        CcToolchainProvider,
        FeatureConfigurationForStarlark,
        CcCompilationContext,
        CcCompilationOutputs,
        CcLinkingOutputs,
        LtoBackendArtifacts,
        LinkerInput,
        LibraryToLink,
        CcLinkingContext,
        CcToolchainVariables,
        CcToolchainConfigInfo,
        CcDebugInfoContext,
        CppModuleMap> {

  @Override
  public CppSemantics getSemantics() {
    return BazelCppSemantics.CPP;
  }

  @Override
  public CppSemantics getSemantics(Language language) {
    return (language == Language.CPP) ? BazelCppSemantics.CPP : BazelCppSemantics.OBJC;
  }

  @Override
  public CcLinkingOutputs link(
      StarlarkActionFactory actions,
      FeatureConfigurationForStarlark starlarkFeatureConfiguration,
      CcToolchainProvider starlarkCcToolchainProvider,
      Object compilationOutputs,
      Sequence<?> userLinkFlags, // <String> expected
      Sequence<?> linkingContexts, // <CcLinkingContext> expected
      String name,
      String language,
      String outputType,
      boolean linkDepsStatically,
      StarlarkInt stamp,
      Object additionalInputs, // <Artifact> expected
      Object grepIncludes,
      Object linkArtifactNameSuffix,
      Object neverLink,
      Object alwaysLink,
      Object testOnlyTarget,
      Object variablesExtension,
      Object nativeDeps,
      Object wholeArchive,
      Object additionalLinkstampDefines,
      Object onlyForDynamicLibs,
      Object mainOutput,
      Object linkerOutputs,
      Object useTestOnlyFlags,
      Object pdbFile,
      Object winDefFile,
      StarlarkThread thread)
      throws InterruptedException, EvalException {
    return super.link(
        actions,
        starlarkFeatureConfiguration,
        starlarkCcToolchainProvider,
        convertFromNoneable(compilationOutputs, /* defaultValue= */ null),
        userLinkFlags,
        linkingContexts,
        name,
        language,
        outputType,
        linkDepsStatically,
        stamp,
        additionalInputs,
        /* grepIncludes= */ null,
        linkArtifactNameSuffix,
        neverLink,
        alwaysLink,
        testOnlyTarget,
        variablesExtension,
        nativeDeps,
        wholeArchive,
        additionalLinkstampDefines,
        onlyForDynamicLibs,
        mainOutput,
        linkerOutputs,
        useTestOnlyFlags,
        pdbFile,
        winDefFile,
        Starlark.UNBOUND,
        Starlark.UNBOUND,
        thread);
  }

  @Override
  public CcCompilationOutputs createCompilationOutputsFromStarlark(
      Object objectsObject,
      Object picObjectsObject,
      Object ltoCopmilationContextObject,
      StarlarkThread thread)
      throws EvalException {
    return super.createCompilationOutputsFromStarlark(
        objectsObject, picObjectsObject, ltoCopmilationContextObject, thread);
  }

  @Override
  public CcCompilationOutputs mergeCcCompilationOutputsFromStarlark(Sequence<?> compilationOutputs)
      throws EvalException {
    CcCompilationOutputs.Builder ccCompilationOutputsBuilder = CcCompilationOutputs.builder();
    for (CcCompilationOutputs ccCompilationOutputs :
        Sequence.cast(compilationOutputs, CcCompilationOutputs.class, "compilation_outputs")) {
      ccCompilationOutputsBuilder.merge(ccCompilationOutputs);
    }
    return ccCompilationOutputsBuilder.build();
  }
}
