/**
 * Copyright (C) 2018 Scott Feldstein
 *
 * Permission is hereby granted, free of charge, to any person obtaining a 
 * copy of this software and associated documentation files (the "Software"), 
 * to deal in the Software without restriction, including without limitation
 * the rights to use, copy, modify, merge, publish, distribute, sublicense,
 * and/or sell copies of the Software, and to permit persons to whom the
 * Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included
 * in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS
 * OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package com.scottieknows.ignite.springdata;

import java.io.Serializable;

import org.apache.ignite.cache.query.annotations.QuerySqlField;
import org.apache.ignite.cache.query.annotations.QueryTextField;

//@Entity
//@Table(name="person")
public class Person implements Serializable {

//    @Id
    @QuerySqlField(index = true)
    public Long id;

    /** Organization ID (indexed). */
    @QuerySqlField(index = true)
    public Long orgId;

    /** First name (not-indexed). */
    @QuerySqlField
    public String firstName;

    /** Last name (not indexed). */
    @QuerySqlField
    public String lastName;

    /** Resume text (create LUCENE-based TEXT index for this field). */
    @QueryTextField
    public String resume;

    /** Salary (indexed). */
    @QuerySqlField(index = true)
    public double salary;

    /**
     * Custom cache key to guarantee that person is always collocated with its
     * organization.
    @Transient
    private transient AffinityKey<Long> key;
     */

    /**
     * * Default constructor.
     */
    public Person() {
        // No-op.
    }

    /**
     * * Constructs person record. * * @param id Person ID. * @param orgId
     * Organization ID. * @param firstName First name. * @param lastName Last
     * name. * @param salary Salary. * @param resume Resume text.
     */
    public Person(Long id, Long orgId, String firstName, String lastName, double salary, String resume) {
        this.id = id;
        this.orgId = orgId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.salary = salary;
        this.resume = resume;
    }

    /**
     * * Constructs person record. * * @param id Person ID. * @param firstName
     * First name. * @param lastName Last name.
     */
    public Person(Long id, String firstName, String lastName) {
        this.id = id;

        this.firstName = firstName;
        this.lastName = lastName;
    }

    /**
     * Gets cache affinity key. Since in some examples person needs to be
     * collocated with organization, we create * custom affinity key to
     * guarantee this collocation. * * @return Custom affinity key to guarantee
     * that person is always collocated with organization.
    @Transient
    public AffinityKey<Long> key() {
        if (key == null)
            key = new AffinityKey<>(id, orgId);

        return key;
    }
     */

    public Long getId() {
        return id;
    }

    public Long getOrgId() {
        return orgId;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getResume() {
        return resume;
    }

    public double getSalary() {
        return salary;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setOrgId(Long orgId) {
        this.orgId = orgId;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setResume(String resume) {
        this.resume = resume;
    }

    public void setSalary(double salary) {
        this.salary = salary;
    }

    @Override
    public String toString() {
        return "Person [id=" + id + ", orgId=" + orgId + ", firstName=" + firstName + ", lastName=" + lastName
            + ", resume=" + resume + ", salary=" + salary + "]";
    }

}
