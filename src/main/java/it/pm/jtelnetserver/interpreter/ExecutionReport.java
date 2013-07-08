
package it.pm.jtelnetserver.interpreter;

/**
 * It defines the execution report class containing the most relevant information
 * coming from a generic execution phase,
 * 
 * @author Paolo Maresca <plo.maresca@gmail.com>
 */
public class ExecutionReport 
{
    private String executionOutput;
    private boolean success;

    public ExecutionReport() 
    {
        super();
        executionOutput = "";
        success = false;
    }
    
    /**
     * Get the execution output.
     * 
     * @return A string containing the execution output
     */
    public String getExecutionOutput() 
    {
        return executionOutput;
    }

    /**
     * Set the execution output.
     * 
     * @param executionOutput A string containing the execution output to be stored
     */
    public void setExecutionOutput(String executionOutput) 
    {
        this.executionOutput = executionOutput;
    }

    /**
     * It reports the command executions status whether it is a success or a 
     * failure.
     * 
     * @return True, iff the command has been successfully executed
     */
    public boolean isSuccess() 
    {
        return success;
    }

    /**
     * It sets the command executions status.
     * 
     * @param success True, if the command has been successfully handled
     */
    public void setSuccess(boolean success) 
    {
        this.success = success;
    }

    /**
     * Stringify the execution report class content.
     * 
     * @return A string representation of this instance
     */
    @Override
    public String toString() 
    {
        return "ExecutionReport{" + "executionOutput=" + executionOutput + '}';
    }

    /**
     * Check if the two istances have the same internal state.
     * 
     * @param obj Object to be compared
     * 
     * @return True, iff the istances are equal ones
     */
    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final ExecutionReport other = (ExecutionReport) obj;
        if ((this.executionOutput == null) ? (other.executionOutput != null) : !this.executionOutput.equals(other.executionOutput)) {
            return false;
        }
        if (this.success != other.success) {
            return false;
        }
        return true;
    }
    
}
