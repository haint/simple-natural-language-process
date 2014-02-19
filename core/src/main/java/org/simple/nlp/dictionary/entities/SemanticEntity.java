/*
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */
package org.simple.nlp.dictionary.entities;

import java.io.Serializable;
import java.util.UUID;

/**
 * @author <a href="mailto:haithanh0809@gmail.com">Nguyen Thanh Hai</a>
 *
 * Feb 18, 2014
 */
public abstract class SemanticEntity implements Serializable {

    /** .*/
    private static final long serialVersionUID = 1L;

    /** .*/
    protected String name;
    
    /** .*/
    protected String[] variants, keywords;
    
    /** .*/
    private String uuid;
    
    public SemanticEntity() {
        this.uuid = UUID.randomUUID().toString();
    }
    
    public String getUUID() {
        return uuid;
    }
    
    public abstract String getEntityType();

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String[] getVariants() {
        return variants;
    }

    public void setVariants(String[] variants) {
        this.variants = variants;
    }

    public String[] getKeywords() {
        return keywords;
    }

    public void setKeywords(String[] keywords) {
        this.keywords = keywords;
    }
    
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName())
            .append("name=").append(name)
            .append(", entity-type=").append(getEntityType())
            .append(", variants=").append(variants)
            .append(", keywords=").append(keywords);
        return sb.toString();
    }
    
    @Override
    public int hashCode() {
        return toString().hashCode();
    }
    
    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        else if (obj instanceof SemanticEntity) {
            SemanticEntity that = (SemanticEntity) obj;
            return that.hashCode() == this.hashCode();
        }
        return false;
    }
}
