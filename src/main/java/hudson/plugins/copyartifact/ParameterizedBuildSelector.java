/*
 * The MIT License
 *
 * Copyright (c) 2004-2011, Sun Microsystems, Inc., Alan Harder
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package hudson.plugins.copyartifact;

import java.util.logging.Level;
import java.util.logging.Logger;

import hudson.EnvVars;
import hudson.Extension;
import hudson.model.Descriptor;
import hudson.model.Job;
import hudson.model.Run;

import org.kohsuke.stapler.DataBoundConstructor;

/**
 * Use a parameter to specify how the build is selected.
 * @see BuildSelectorParameter
 * @author Alan Harder
 */
public class ParameterizedBuildSelector extends BuildSelector {
    private String parameterName;
    private static final Logger LOG = Logger.getLogger(ParameterizedBuildSelector.class.getName());

    @DataBoundConstructor
    public ParameterizedBuildSelector(String parameterName) {
        this.parameterName = parameterName;
    }

    public String getParameterName() {
        return parameterName;
    }

    @Override
    public Run<?,?> getBuild(Job<?,?> job, EnvVars env, BuildFilter filter, Run<?,?> parent) {
        String xml = env.get(getParameterName());
        if (xml == null) {
            LOG.log(Level.WARNING, "{0} is not defined", getParameterName());
            return null;
        }
        return BuildSelectorParameter.getSelectorFromXml(xml)
                                     .getBuild(job, env, filter, parent);
    }

    @Extension(ordinal=-20)
    public static final Descriptor<BuildSelector> DESCRIPTOR =
            new SimpleBuildSelectorDescriptor(
                ParameterizedBuildSelector.class, Messages._ParameterizedBuildSelector_DisplayName());
}
