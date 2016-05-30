package com.pyango.tree.structure;

import com.intellij.ide.projectView.*;
import com.intellij.ide.projectView.impl.nodes.PsiDirectoryNode;
import com.intellij.ide.projectView.impl.nodes.PsiFileNode;
import com.intellij.psi.PsiDirectory;
import com.intellij.ide.util.treeView.AbstractTreeNode;
import com.intellij.openapi.fileTypes.PlainTextFileType;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import org.jetbrains.annotations.*;

import java.util.*;

/**
 * @author Anna Bulenkova
 */
public class CustomTreeStructureProvider implements TreeStructureProvider {
    @NotNull
    @Override
    public Collection<AbstractTreeNode> modify(@NotNull AbstractTreeNode parent,
                                               @NotNull Collection<AbstractTreeNode> children,
                                               ViewSettings settings) {
        ArrayList<AbstractTreeNode> nodes = new ArrayList<AbstractTreeNode>();
        for (AbstractTreeNode child : children) {
            Project project = child.getProject();
            if (project != null) {
                if (child.getValue() instanceof PsiDirectory) {
                    PsiDirectory directory = (PsiDirectory) child.getValue();
                    nodes.add(new PsiDirectoryNodeCustom(project, directory, settings));
                    continue;
                }
                nodes.add(child);
            }
        }
        return nodes;
    }

    @Nullable
    @Override
    public Object getData(Collection<AbstractTreeNode> collection, String s) {
        return null;
    }
}
