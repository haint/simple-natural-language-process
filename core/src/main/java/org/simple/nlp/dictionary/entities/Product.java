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

/**
 * @author <a href="mailto:haithanh0809@gmail.com">Nguyen Thanh Hai</a>
 *
 * Feb 19, 2014
 */
public class Product extends SemanticEntity {

    /** .*/
    private static final long serialVersionUID = 1L;

    /** .*/
    public static final String ENTITY_TYPE = "product";
    
    /** .*/
    protected String[] type;
    
    /** .*/
    protected String model, maker, classifier;
    
    @Override
    public String getEntityType() {
        return ENTITY_TYPE;
    }
    
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(super.toString());
        sb.append(", maker=").append(maker)
            .append(", model=").append(model)
            .append(", classifier=").append(classifier)
            .append(", type=").append(type);
        return sb.toString();
    }
}
