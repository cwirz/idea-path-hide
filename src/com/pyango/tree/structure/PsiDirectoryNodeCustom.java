package com.pyango.tree.structure;

import com.intellij.ide.projectView.PresentationData;
import com.intellij.ide.projectView.ViewSettings;
import com.intellij.ide.projectView.impl.ProjectRootsUtil;
import com.intellij.ide.projectView.impl.nodes.ProjectViewDirectoryHelper;
import com.intellij.ide.projectView.impl.nodes.PsiDirectoryNode;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.roots.ProjectFileIndex;
import com.intellij.openapi.roots.ProjectRootManager;
import com.intellij.openapi.util.Comparing;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.pom.NavigatableWithText;
import com.intellij.psi.PsiDirectory;
import com.intellij.ui.SimpleTextAttributes;


public class PsiDirectoryNodeCustom extends PsiDirectoryNode {
    public PsiDirectoryNodeCustom(Project project, PsiDirectory value, ViewSettings viewSettings) {
        super(project, value, viewSettings);
    }

    @Override
    protected void updateImpl(PresentationData data) {
        Project project = this.getProject();

        assert project != null : this;

        PsiDirectory psiDirectory = (PsiDirectory)this.getValue();

        assert psiDirectory != null : this;

        VirtualFile directoryFile = psiDirectory.getVirtualFile();
        Object parentValue = this.getParentValue();
        if(ProjectRootsUtil.isModuleContentRoot(directoryFile, project)) {
            ProjectFileIndex name = ProjectRootManager.getInstance(project).getFileIndex();
            Module module = name.getModuleForFile(directoryFile);
            data.setPresentableText(directoryFile.getName());
            if(module != null) {
                if(!(parentValue instanceof Module)) {
                    if(!this.shouldShowModuleName()) {
                        data.addText(directoryFile.getName() + " ", SimpleTextAttributes.REGULAR_ATTRIBUTES);
                    } else if(Comparing.equal(module.getName(), directoryFile.getName())) {
                        data.addText(directoryFile.getName(), SimpleTextAttributes.REGULAR_BOLD_ATTRIBUTES);
                    } else {
                        data.addText(directoryFile.getName() + " ", SimpleTextAttributes.REGULAR_ATTRIBUTES);
                        data.addText("[" + module.getName() + "]", SimpleTextAttributes.REGULAR_BOLD_ATTRIBUTES);
                    }
                } else {
                    data.addText(directoryFile.getName(), SimpleTextAttributes.REGULAR_ATTRIBUTES);
                }

                boolean shouldShowUrl = false;
                data.setLocationString(ProjectViewDirectoryHelper.getInstance(project).getLocationString(psiDirectory, shouldShowUrl, this.shouldShowSourcesRoot()));
                this.setupIcon(data, psiDirectory);
                return;
            }
        }
        String name1 = parentValue instanceof Project?psiDirectory.getVirtualFile().getPresentableUrl():ProjectViewDirectoryHelper.getInstance(psiDirectory.getProject()).getNodeName(this.getSettings(), parentValue, psiDirectory);
        if(name1 == null) {
            this.setValue(null);
        } else {
            data.setPresentableText(name1);
            data.setLocationString(ProjectViewDirectoryHelper.getInstance(project).getLocationString(psiDirectory, false, false));
            this.setupIcon(data, psiDirectory);
        }
    }
}